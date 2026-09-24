package pe.edu.upeu.MatriculaBackend.service.service;

import pe.edu.upeu.MatriculaBackend.dto.CursoRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CursoResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.generic.CrudService;

import java.util.List;

public interface CursoService extends CrudService<CursoRequestDTO, CursoResponseDTO, Long> {

    // RF-03: Listar cursos pertenecientes a una carrera específica
    List<CursoResponseDTO> listarPorCarrera(Long carreraId);

    // RF-06: Búsqueda dinámica con filtros combinables y ordenamiento
    List<CursoResponseDTO> buscar(String nombre, Long carreraId, Integer ciclo, Boolean conVacantes, String orden, String dir);
}
