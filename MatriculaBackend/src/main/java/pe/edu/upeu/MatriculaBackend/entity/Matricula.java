package pe.edu.upeu.MatriculaBackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matriculas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 10)
    @Pattern(regexp = "^\\d{4}-[12]$", message = "El periodo debe tener el formato YYYY-1 o YYYY-2 (ej. 2026-2)")
    private String periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMatricula estado;

    @Column(name = "total_creditos", nullable = false)
    private Integer totalCreditos;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DetalleMatricula> detalles = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fecha = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public void agregarDetalle(DetalleMatricula detalle) {
        detalles.add(detalle);
        detalle.setMatricula(this);
    }
}