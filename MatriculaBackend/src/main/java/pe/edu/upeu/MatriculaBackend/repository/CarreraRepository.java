package pe.edu.upeu.MatriculaBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.MatriculaBackend.entity.Carrera;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}