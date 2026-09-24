package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.EstudianteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Endpoints para la gestión de estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @PostMapping
    @Operation(summary = "Registrar nuevo estudiante")
    public ResponseEntity<EstudianteResponseDTO> create(@Valid @RequestBody EstudianteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estudiante existente")
    public ResponseEntity<EstudianteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EstudianteRequestDTO dto) {
        return ResponseEntity.ok(estudianteService.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID")
    public ResponseEntity<EstudianteResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.read(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los estudiantes")
    public ResponseEntity<List<EstudianteResponseDTO>> readAll() {
        return ResponseEntity.ok(estudianteService.readAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estudiante")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
