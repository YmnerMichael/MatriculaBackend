package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.MatriculaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
@Tag(name = "Matrículas", description = "Endpoints para el registro, consulta y anulación de matrículas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    @Operation(summary = "Registrar nueva matrícula cabecera-detalle")
    public ResponseEntity<MatriculaResponseDTO> registrar(@Valid @RequestBody MatriculaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.registrar(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener matrícula por ID con sus detalles")
    public ResponseEntity<MatriculaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las matrículas")
    public ResponseEntity<List<MatriculaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(matriculaService.listarTodas());
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular matrícula y restaurar vacantes")
    public ResponseEntity<MatriculaResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.anular(id));
    }
}
