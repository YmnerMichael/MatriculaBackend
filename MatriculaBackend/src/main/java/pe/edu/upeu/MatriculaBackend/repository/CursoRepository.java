package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.MatriculaBackend.entity.Curso;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long>, JpaSpecificationExecutor<Curso> {

    // RF-03: Listar cursos por ID de carrera
    List<Curso> findByCarreraId(Long carreraId);

    // Validación de código único
    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    // Verificar si la carrera tiene cursos asociados (para responder 409 al intentar eliminar carrera)
    boolean existsByCarreraId(Long carreraId);
}