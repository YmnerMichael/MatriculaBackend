package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaRequestDTO {

    @NotNull(message = "El id del estudiante es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(regexp = "^\\d{4}-[12]$", message = "El periodo debe seguir el formato YYYY-1 o YYYY-2 (ej. 2026-2)")
    private String periodo;

    @NotEmpty(message = "La matrícula debe contener al menos un curso")
    @Valid
    private List<DetalleMatriculaRequestDTO> detalles;
}
