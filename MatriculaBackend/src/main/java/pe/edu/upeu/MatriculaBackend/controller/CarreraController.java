package pe.edu.upeu.MatriculaBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.MatriculaBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.MatriculaBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.MatriculaBackend.service.service.CarreraService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carreras")
@RequiredArgsConstructor
@Tag(name = "Carreras", description = "Endpoints para la gestión de carreras profesionales")
public class CarreraController {

    private final CarreraService carreraService;

    @PostMapping
    @Operation(summary = "Registrar nueva carrera")
    public ResponseEntity<CarreraResponseDTO> create(@Valid @RequestBody CarreraRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrera existente")
    public ResponseEntity<CarreraResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CarreraRequestDTO dto) {
        return ResponseEntity.ok(carreraService.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener carrera por ID")
    public ResponseEntity<CarreraResponseDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(carreraService.read(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas las carreras")
    public ResponseEntity<List<CarreraResponseDTO>> readAll() {
        return ResponseEntity.ok(carreraService.readAll());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar carrera")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carreraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
