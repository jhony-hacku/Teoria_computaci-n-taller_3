package edu.javeriana.estudiante_notas.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import edu.javeriana.estudiante_notas.modelo.Estudiante;

@Repository
public interface RepositorioEstudiante extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByCorreo(String correo);
}
