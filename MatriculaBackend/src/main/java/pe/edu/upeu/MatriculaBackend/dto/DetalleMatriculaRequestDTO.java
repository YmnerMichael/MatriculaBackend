package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleMatriculaRequestDTO {

    @NotNull(message = "El id del curso es obligatorio")
    private Long cursoId;
}
