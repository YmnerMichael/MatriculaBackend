package pe.edu.upeu.MatriculaBackend.service.service;

import pe.edu.upeu.MatriculaBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaResponseDTO;

import java.util.List;

public interface MatriculaService {

    // RF-05: Registrar matrícula cabecera-detalle
    MatriculaResponseDTO registrar(MatriculaRequestDTO request);

    // RF-05: Consultar por ID
    MatriculaResponseDTO obtenerPorId(Long id);

    // RF-05: Listar todas las matrículas
    List<MatriculaResponseDTO> listarTodas();

    // RF-05: Anular matrícula y devolver vacantes
    MatriculaResponseDTO anular(Long id);
}
