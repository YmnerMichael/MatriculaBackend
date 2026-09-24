package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;

import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    // RF-02: Nombre único sin distinguir mayúsculas ni espacios
    boolean existsByNombreIgnoreCase(String nombre);

    // Para validación al actualizar (excluyendo el id actual)
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    Optional<Carrera> findByNombreIgnoreCase(String nombre);
}
