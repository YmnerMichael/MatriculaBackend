package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.MatriculaBackend.repository.MatriculaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Endpoints de reportes consolidados del sistema")
public class ReporteController {

    private final MatriculaRepository matriculaRepository;

    @GetMapping("/matriculados-por-curso")
    @Operation(summary = "Reporte de matriculados y recaudación por curso para un periodo y carrera")
    public ResponseEntity<List<MatriculadosPorCursoDTO>> obtenerReporteMatriculadosPorCurso(
            @RequestParam String periodo,
            @RequestParam Long carreraId) {
        return ResponseEntity.ok(matriculaRepository.obtenerReporteMatriculadosPorCurso(periodo, carreraId));
    }
}
