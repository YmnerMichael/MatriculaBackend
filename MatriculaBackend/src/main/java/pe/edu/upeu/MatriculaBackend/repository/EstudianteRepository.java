package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.MatriculaBackend.entity.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // RF-04: Código y DNI únicos
    boolean existsByCodigo(String codigo);

    boolean existsByDni(String dni);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    boolean existsByDniAndIdNot(String dni, Long id);

    // Listar estudiantes por carrera
    List<Estudiante> findByCarreraId(Long carreraId);
}
