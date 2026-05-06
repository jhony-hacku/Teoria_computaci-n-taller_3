package edu.javeriana.estudiante_notas.repositorio;

import edu.javeriana.estudiante_notas.modelo.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RepositorioNota extends JpaRepository <Nota, Long> {

    List<Nota> findByEstudianteId(Long id);
    void deleteByEstudianteId(Long estudianteId);
}
