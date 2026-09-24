package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.entity.Estudiante;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.EstudianteRepository;
import pe.edu.upeu.MatriculaBackend.service.service.EstudianteService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;

    @Override
    @Transactional
    public EstudianteResponseDTO create(EstudianteRequestDTO dto) {
        if (estudianteRepository.existsByCodigo(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el código: " + dto.getCodigo());
        }
        if (estudianteRepository.existsByDni(dto.getDni())) {
            throw new ReglaNegocioException("Ya existe un estudiante con el DNI: " + dto.getDni());
        }

        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + dto.getCarreraId()));

        Estudiante estudiante = Estudiante.builder()
                .codigo(dto.getCodigo())
                .dni(dto.getDni())
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .estado(dto.getEstado())
                .carrera(carrera)
                .build();

        return mapearADTO(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional
    public EstudianteResponseDTO update(Long id, EstudianteRequestDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id));

        if (estudianteRepository.existsByCodigoAndIdNot(dto.getCodigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro estudiante registrado con el código: " + dto.getCodigo());
        }

        if (estudianteRepository.existsByDniAndIdNot(dto.getDni(), id)) {
            throw new ReglaNegocioException("Ya existe otro estudiante registrado con el DNI: " + dto.getDni());
        }

        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + dto.getCarreraId()));

        estudiante.setCodigo(dto.getCodigo());
        estudiante.setDni(dto.getDni());
        estudiante.setNombres(dto.getNombres());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setEmail(dto.getEmail());
        estudiante.setEstado(dto.getEstado());
        estudiante.setCarrera(carrera);

        return mapearADTO(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDTO read(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id));
        return mapearADTO(estudiante);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!estudianteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> readAll() {
        return estudianteRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private EstudianteResponseDTO mapearADTO(Estudiante e) {
        return EstudianteResponseDTO.builder()
                .id(e.getId())
                .codigo(e.getCodigo())
                .dni(e.getDni())
                .nombres(e.getNombres())
                .apellidos(e.getApellidos())
                .email(e.getEmail())
                .estado(e.getEstado())
                .carreraId(e.getCarrera().getId())
                .carreraNombre(e.getCarrera().getNombre())
                .fechaCreacion(e.getFechaCreacion())
                .fechaModificacion(e.getFechaModificacion())
                .build();
    }
}