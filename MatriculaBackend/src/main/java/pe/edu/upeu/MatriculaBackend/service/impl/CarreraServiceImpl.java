package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.repository.CursoRepository;
import pe.edu.upeu.MatriculaBackend.service.service.CarreraService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final CursoRepository cursoRepository;

    @Override
    @Transactional
    public CarreraResponseDTO create(CarreraRequestDTO dto) {
        // RF-02: Nombre único sin distinguir mayúsculas ni espacios
        String nombreLimpio = dto.getNombre().trim();
        if (carreraRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ReglaNegocioException("Ya existe una carrera registrada con el nombre: " + nombreLimpio);
        }

        Carrera carrera = Carrera.builder()
                .nombre(nombreLimpio)
                .descripcion(dto.getDescripcion())
                .estado(dto.getEstado())
                .build();

        return mapearADTO(carreraRepository.save(carrera));
    }

    @Override
    @Transactional
    public CarreraResponseDTO update(Long id, CarreraRequestDTO dto) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id));

        String nombreLimpio = dto.getNombre().trim();
        if (carreraRepository.existsByNombreIgnoreCaseAndIdNot(nombreLimpio, id)) {
            throw new ReglaNegocioException("Ya existe otra carrera registrada con el nombre: " + nombreLimpio);
        }

        carrera.setNombre(nombreLimpio);
        carrera.setDescripcion(dto.getDescripcion());
        carrera.setEstado(dto.getEstado());

        return mapearADTO(carreraRepository.save(carrera));
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO read(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id));
        return mapearADTO(carrera);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!carreraRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Carrera no encontrada con id: " + id);
        }
        // Validar asociación con cursos para responder 409
        if (cursoRepository.existsByCarreraId(id)) {
            throw new ReglaNegocioException("No se puede eliminar la carrera porque tiene cursos asociados.");
        }
        carreraRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> readAll() {
        return carreraRepository.findAll().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    private CarreraResponseDTO mapearADTO(Carrera c) {
        return CarreraResponseDTO.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .descripcion(c.getDescripcion())
                .estado(c.getEstado())
                .fechaCreacion(c.getFechaCreacion())
                .fechaModificacion(c.getFechaModificacion())
                .build();
    }
}
