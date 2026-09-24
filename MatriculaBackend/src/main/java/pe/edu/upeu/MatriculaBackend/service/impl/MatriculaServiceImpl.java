package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.*;
import pe.edu.upeu.MatriculaBackend.entity.*;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.*;
import pe.edu.upeu.MatriculaBackend.service.service.MatriculaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    @Value("${matricula.costo-credito:120.00}")
    private BigDecimal costoCredito;

    @Override
    @Transactional
    public MatriculaResponseDTO registrar(MatriculaRequestDTO request) {
        // 1. Validar estudiante
        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con id: " + request.getEstudianteId()));

        // RN-01: Estudiante activo
        if (!Boolean.TRUE.equals(estudiante.getEstado())) {
            throw new ReglaNegocioException("RN-01: El estudiante no se encuentra activo para matricularse.");
        }

        // RN-03: Solo una matrícula REGISTRADA por periodo
        if (matriculaRepository.existsByEstudianteIdAndPeriodoAndEstado(
                estudiante.getId(), request.getPeriodo(), EstadoMatricula.REGISTRADA)) {
            throw new ReglaNegocioException("RN-03: El estudiante ya cuenta con una matrícula en estado REGISTRADA para el periodo " + request.getPeriodo());
        }

        Matricula matricula = Matricula.builder()
                .periodo(request.getPeriodo())
                .estudiante(estudiante)
                .estado(EstadoMatricula.REGISTRADA)
                .build();

        int totalCreditos = 0;
        BigDecimal montoTotal = BigDecimal.ZERO;

        for (DetalleMatriculaRequestDTO detDto : request.getDetalles()) {
            Curso curso = cursoRepository.findById(detDto.getCursoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con id: " + detDto.getCursoId()));

            // RN-01: Curso activo
            if (!Boolean.TRUE.equals(curso.getEstado())) {
                throw new ReglaNegocioException("RN-01: El curso " + curso.getCodigo() + " no está activo.");
            }

            // RN-01: El curso debe pertenecer a la carrera del estudiante
            if (!curso.getCarrera().getId().equals(estudiante.getCarrera().getId())) {
                throw new ReglaNegocioException("RN-01: El curso " + curso.getCodigo() + " no pertenece a la carrera del estudiante.");
            }

            // RN-02: Vacantes disponibles
            if (curso.getVacantes() == null || curso.getVacantes() <= 0) {
                throw new ReglaNegocioException("RN-02: El curso " + curso.getCodigo() + " no tiene vacantes disponibles.");
            }

            // Descontar vacante
            curso.setVacantes(curso.getVacantes() - 1);

            // RN-04: Calcular costo = créditos x costoCredito (HALF_UP a 2 decimales)
            BigDecimal costoCurso = costoCredito.multiply(BigDecimal.valueOf(curso.getCreditos()))
                    .setScale(2, RoundingMode.HALF_UP);

            DetalleMatricula detalle = DetalleMatricula.builder()
                    .curso(curso)
                    .creditos(curso.getCreditos())
                    .costo(costoCurso)
                    .build();

            matricula.agregarDetalle(detalle);

            totalCreditos += curso.getCreditos();
            montoTotal = montoTotal.add(costoCurso);
        }

        // RN-04: Límite de 20 créditos
        if (totalCreditos > 20) {
            throw new ReglaNegocioException("RN-04: La matrícula no puede superar el límite de 20 créditos. Total solicitado: " + totalCreditos);
        }

        matricula.setTotalCreditos(totalCreditos);
        matricula.setMontoTotal(montoTotal);

        Matricula guardada = matriculaRepository.save(matricula);
        return mapearADTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public MatriculaResponseDTO obtenerPorId(Long id) {
        Matricula m = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con id: " + id));
        return mapearADTO(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarTodas() {
        return matriculaRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MatriculaResponseDTO anular(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con id: " + id));

        if (matricula.getEstado() == EstadoMatricula.ANULADA) {
            throw new ReglaNegocioException("La matrícula ya se encuentra en estado ANULADA.");
        }

        // RN-02: Devolver vacantes a los cursos
        for (DetalleMatricula detalle : matricula.getDetalles()) {
            Curso curso = detalle.getCurso();
            curso.setVacantes(curso.getVacantes() + 1);
        }

        matricula.setEstado(EstadoMatricula.ANULADA);
        return mapearADTO(matriculaRepository.save(matricula));
    }

    private MatriculaResponseDTO mapearADTO(Matricula m) {
        List<DetalleMatriculaResponseDTO> detallesDto = m.getDetalles().stream()
                .map(d -> DetalleMatriculaResponseDTO.builder()
                        .id(d.getId())
                        .cursoId(d.getCurso().getId())
                        .cursoCodigo(d.getCurso().getCodigo())
                        .cursoNombre(d.getCurso().getNombre())
                        .creditos(d.getCreditos())
                        .costo(d.getCosto())
                        .build())
                .collect(Collectors.toList());

        return MatriculaResponseDTO.builder()
                .id(m.getId())
                .fecha(m.getFecha())
                .periodo(m.getPeriodo())
                .estudianteId(m.getEstudiante().getId())
                .estudianteNombreCompleto(m.getEstudiante().getNombres() + " " + m.getEstudiante().getApellidos())
                .estado(m.getEstado().name())
                .totalCreditos(m.getTotalCreditos())
                .montoTotal(m.getMontoTotal())
                .detalles(detallesDto)
                .fechaCreacion(m.getFechaCreacion())
                .fechaModificacion(m.getFechaModificacion())
                .build();
    }
}