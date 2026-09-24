package pe.edu.upeu.MatriculaBackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.MatriculaBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;
import pe.edu.upeu.MatriculaBackend.exception.ReglaNegocioException;
import pe.edu.upeu.MatriculaBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.MatriculaBackend.repository.CarreraRepository;
import pe.edu.upeu.MatriculaBackend.service.service.CarreraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;

    @Override
    @Transactional
    public CarreraResponseDTO create(CarreraRequestDTO dto) {
        // Limpiar espacios en blanco al inicio y al final
        String nombreLimpio = dto.getNombre() != null ? dto.getNombre().trim() : "";

        // Validar si la carrera ya existe (case-insensitive)
        if (carreraRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ReglaNegocioException("La carrera con el nombre '" + nombreLimpio + "' ya existe.");
        }

        Carrera carrera = new Carrera();
        carrera.setNombre(nombreLimpio);
        carrera.setDescripcion(dto.getDescripcion());
        carrera.setEstado(dto.getEstado() != null ? dto.getEstado() : true);

        Carrera saved = carreraRepository.save(carrera);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public CarreraResponseDTO update(Long id, CarreraRequestDTO dto) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id));

        String nombreLimpio = dto.getNombre() != null ? dto.getNombre().trim() : "";

        if (!carrera.getNombre().equalsIgnoreCase(nombreLimpio) && carreraRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new ReglaNegocioException("La carrera con el nombre '" + nombreLimpio + "' ya existe.");
        }

        carrera.setNombre(nombreLimpio);
        carrera.setDescripcion(dto.getDescripcion());
        carrera.setEstado(dto.getEstado());

        return mapToDTO(carreraRepository.save(carrera));
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO read(Long id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con id: " + id));
        return mapToDTO(carrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> readAll() {
        return carreraRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!carreraRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Carrera no encontrada con id: " + id);
        }
        carreraRepository.deleteById(id);
    }

    private CarreraResponseDTO mapToDTO(Carrera carrera) {
        CarreraResponseDTO dto = new CarreraResponseDTO();
        dto.setId(carrera.getId());
        dto.setNombre(carrera.getNombre());
        dto.setDescripcion(carrera.getDescripcion());
        dto.setEstado(carrera.getEstado());
        dto.setFechaCreacion(carrera.getFechaCreacion());
        dto.setFechaModificacion(carrera.getFechaModificacion());
        return dto;
    }
}
