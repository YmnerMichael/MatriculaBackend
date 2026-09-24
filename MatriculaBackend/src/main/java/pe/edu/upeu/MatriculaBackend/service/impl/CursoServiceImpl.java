package pe.edu.upeu.MatriculaBackend.service.impl;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.CursoRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CursoResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.entity.Curso;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.CursoRepository;
import pe.edu.upeu.MatriculaBackend.repository.MatriculaRepository;
import pe.edu.upeu.MatriculaBackend.service.service.CursoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;
    private final MatriculaRepository matriculaRepository;

    @Override
    @Transactional
    public CursoResponseDTO create(CursoRequestDTO dto) {
        if (cursoRepository.existsByCodigo(dto.getCodigo())) {
            throw new ReglaNegocioException("Ya existe un curso registrado con el código: " + dto.getCodigo());
        }

        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + dto.getCarreraId()));

        Curso curso = Curso.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .creditos(dto.getCreditos())
                .ciclo(dto.getCiclo())
                .vacantes(dto.getVacantes())
                .estado(dto.getEstado())
                .carrera(carrera)
                .build();

        return mapearADTO(cursoRepository.save(curso));
    }

    @Override
    @Transactional
    public CursoResponseDTO update(Long id, CursoRequestDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con id: " + id));

        if (cursoRepository.existsByCodigoAndIdNot(dto.getCodigo(), id)) {
            throw new ReglaNegocioException("Ya existe otro curso con el código: " + dto.getCodigo());
        }

        Carrera carrera = carreraRepository.findById(dto.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + dto.getCarreraId()));

        curso.setCodigo(dto.getCodigo());
        curso.setNombre(dto.getNombre());
        curso.setCreditos(dto.getCreditos());
        curso.setCiclo(dto.getCiclo());
        curso.setVacantes(dto.getVacantes());
        curso.setEstado(dto.getEstado());
        curso.setCarrera(carrera);

        return mapearADTO(cursoRepository.save(curso));
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponseDTO read(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con id: " + id));
        return mapearADTO(curso);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Curso no encontrado con id: " + id);
        }
        if (matriculaRepository.existsByDetallesCursoId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el curso porque tiene matrículas registradas.");
        }
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> readAll() {
        return cursoRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> listarPorCarrera(Long carreraId) {
        if (!carreraRepository.existsById(carreraId)) {
            throw new RecursoNoEncontradoException("Carrera no encontrada con id: " + carreraId);
        }
        return cursoRepository.findByCarreraId(carreraId).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> buscar(String nombre, Long carreraId, Integer ciclo, Boolean conVacantes, String orden, String dir) {
        Specification<Curso> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }
            if (carreraId != null) {
                predicates.add(cb.equal(root.get("carrera").get("id"), carreraId));
            }
            if (ciclo != null) {
                predicates.add(cb.equal(root.get("ciclo"), ciclo));
            }
            if (Boolean.TRUE.equals(conVacantes)) {
                predicates.add(cb.greaterThan(root.get("vacantes"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // Validar campo de ordenamiento
        String campoOrden = (orden != null && List.of("nombre", "creditos", "vacantes").contains(orden.toLowerCase()))
                ? orden.toLowerCase() : "nombre";

        Sort.Direction direccion = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;

        return cursoRepository.findAll(spec, Sort.by(direccion, campoOrden)).stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private CursoResponseDTO mapearADTO(Curso c) {
        return CursoResponseDTO.builder()
                .id(c.getId())
                .codigo(c.getCodigo())
                .nombre(c.getNombre())
                .creditos(c.getCreditos())
                .ciclo(c.getCiclo())
                .vacantes(c.getVacantes())
                .estado(c.getEstado())
                .carreraId(c.getCarrera().getId())
                .carreraNombre(c.getCarrera().getNombre())
                .fechaCreacion(c.getFechaCreacion())
                .fechaModificacion(c.getFechaModificacion())
                .build();
    }
}
