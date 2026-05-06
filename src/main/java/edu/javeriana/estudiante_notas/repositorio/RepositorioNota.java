package edu.javeriana.estudiante_notas.repositorio;

import edu.javeriana.estudiante_notas.modelo.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositorioNota extends JpaRepository <Nota, Long> {
    List<Nota> findByEstudianteId(Long id);
    void deleteByEstudianteId(Long estudianteId);
}
