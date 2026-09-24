package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.CursoRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CursoResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.CursoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "Endpoints para la gestión y búsqueda de cursos")
public class CursoController {

    private final CursoService cursoService;

    @PostMapping("/cursos")
    @Operation(summary = "Registrar nuevo curso")
    public ResponseEntity<CursoResponseDTO> create(@Valid @RequestBody CursoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.create(dto));
    }

    @PutMapping("/cursos/{id}")
    @Operation(summary = "Actualizar curso existente")
    public ResponseEntity<CursoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CursoRequestDTO dto) {
        return ResponseEntity.ok(cursoService.update(id, dto));
    }

    @GetMapping("/cursos/{id}")
    @Operation(summary = "Obtener curso por ID")
    public ResponseEntity<CursoResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.read(id));
    }

    @GetMapping("/cursos")
    @Operation(summary = "Listar todos los cursos")
    public ResponseEntity<List<CursoResponseDTO>> readAll() {
        return ResponseEntity.ok(cursoService.readAll());
    }

    @DeleteMapping("/cursos/{id}")
    @Operation(summary = "Eliminar curso")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // RF-03: GET /api/v1/carreras/{id}/cursos
    @GetMapping("/carreras/{carreraId}/cursos")
    @Operation(summary = "Listar cursos pertenecientes a una carrera")
    public ResponseEntity<List<CursoResponseDTO>> listarPorCarrera(@PathVariable Long carreraId) {
        return ResponseEntity.ok(cursoService.listarPorCarrera(carreraId));
    }

    // RF-06: GET /api/v1/cursos/buscar con filtros opcionales
    @GetMapping("/cursos/buscar")
    @Operation(summary = "Buscar cursos con filtros opcionales y combinables")
    public ResponseEntity<List<CursoResponseDTO>> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Long carreraId,
            @RequestParam(required = false) Integer ciclo,
            @RequestParam(required = false) Boolean conVacantes,
            @RequestParam(required = false, defaultValue = "nombre") String orden,
            @RequestParam(required = false, defaultValue = "asc") String dir) {
        return ResponseEntity.ok(cursoService.buscar(nombre, carreraId, ciclo, conVacantes, orden, dir));
    }
}
