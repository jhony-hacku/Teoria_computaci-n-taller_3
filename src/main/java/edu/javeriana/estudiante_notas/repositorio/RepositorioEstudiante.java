package edu.javeriana.estudiante_notas.repositorio;
import edu.javeriana.estudiante_notas.modelo.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioEstudiante extends JpaRepository <Estudiante, Long> {

    Optional<Estudiante> findbyCorreo(String correo);
    boolean existsByCorreo(String correo);

}
