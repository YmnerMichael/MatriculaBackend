package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.MatriculaBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.MatriculaBackend.entity.Matricula;
import pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // RN-03: Verificar si el estudiante ya tiene matrícula REGISTRADA en el periodo
    boolean existsByEstudianteIdAndPeriodoAndEstado(Long estudianteId, String periodo, EstadoMatricula estado);

    // Comprobar si un curso tiene matrículas asociadas (para responder 409 al intentar eliminar curso)
    boolean existsByDetallesCursoId(Long cursoId);

    // RF-07: Reporte con JPQL GROUP BY y proyección (Solo matrículas REGISTRADA)
    @Query("""
        SELECT 
            c.codigo AS codigo,
            c.nombre AS curso,
            COUNT(DISTINCT m.id) AS matriculados,
            SUM(d.costo) AS montoRecaudado
        FROM Matricula m
        JOIN m.detalles d
        JOIN d.curso c
        WHERE m.estado = pe.edu.upeu.MatriculaBackend.enums.EstadoMatricula.REGISTRADA
          AND m.periodo = :periodo
          AND c.carrera.id = :carreraId
        GROUP BY c.codigo, c.nombre
    """)
    List<MatriculadosPorCursoDTO> obtenerReporteMatriculadosPorCurso(
            @Param("periodo") String periodo,
            @Param("carreraId") Long carreraId);

    // SC-A: Historial de matrículas de un estudiante ordenadas de la más reciente a la más antigua
    List<EstudianteMatriculasQuery> findByEstudianteIdOrderByFechaDesc(Long estudianteId);

    List<EstudianteMatriculasQuery> findByEstudianteIdAndPeriodoOrderByFechaDesc(Long estudianteId, String periodo);

    interface EstudianteMatriculasQuery {
        // Interfaz de apoyo para proyecciones si fuera necesaria
    }
}