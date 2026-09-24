package pe.edu.upeu.MatriculaBackend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}$", message = "El código debe tener el formato especificado (ej. IS401)")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser como mínimo 1")
    @Max(value = 6, message = "Los créditos deben ser como máximo 6")
    private Integer creditos;

    @NotNull(message = "El ciclo es obligatorio")
    @Min(value = 1, message = "El ciclo debe estar entre 1 y 10")
    @Max(value = 10, message = "El ciclo debe estar entre 1 y 10")
    private Integer ciclo;

    @NotNull(message = "Las vacantes son obligatorias")
    @Min(value = 0, message = "Las vacantes no pueden ser negativas")
    private Integer vacantes;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "El id de la carrera es obligatorio")
    private Long carreraId;
}
