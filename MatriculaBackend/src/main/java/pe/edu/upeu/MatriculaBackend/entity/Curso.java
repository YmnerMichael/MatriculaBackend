package pe.edu.upeu.MatriculaBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    @Pattern(regexp = "^[A-Z]{2}\\d{3}$", message = "El código debe cumplir el patrón (ej. IS401)")
    private String codigo;

    @Column(nullable = false, length = 150)
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @Column(nullable = false)
    @Min(value = 1, message = "Los créditos deben ser como mínimo 1")
    @Max(value = 6, message = "Los créditos deben ser como máximo 6")
    private Integer creditos;

    @Column(nullable = false)
    @Min(value = 1, message = "El ciclo debe ser entre 1 y 10")
    @Max(value = 10, message = "El ciclo debe ser entre 1 y 10")
    private Integer ciclo;

    @Column(nullable = false)
    @Min(value = 0, message = "Las vacantes no pueden ser negativas")
    private Integer vacantes;

    @Column(nullable = false)
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}