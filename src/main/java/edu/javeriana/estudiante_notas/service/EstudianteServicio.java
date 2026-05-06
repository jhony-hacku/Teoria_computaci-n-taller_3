package edu.javeriana.estudiante_notas.service;

import edu.javeriana.estudiante_notas.dto.EstudianteDTO;
import edu.javeriana.estudiante_notas.modelo.Estudiante;
import edu.javeriana.estudiante_notas.repositorio.RepositorioEstudiante;
import edu.javeriana.estudiante_notas.repositorio.RepositorioNota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteServicio {

    @Autowired
    private RepositorioEstudiante repositorioEstudiante;

    @Autowired
    private RepositorioNota repositorioNota;

    // Obtener todos los estudiantes
    public List<EstudianteDTO> obtenerTodos() {
        return repositorioEstudiante.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener estudiante por ID
    public EstudianteDTO obtenerPorId(Long id) {
        Estudiante estudiante = repositorioEstudiante.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));
        return convertirADTO(estudiante);
    }

    // Crear un nuevo estudiante
    public EstudianteDTO crear(EstudianteDTO dto) {
        if (repositorioEstudiante.findByCorreo(dto.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe un estudiante con el correo: " + dto.getCorreo());
        }
        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(dto.getNombre());
        estudiante.setApellido(dto.getApellido());
        estudiante.setCorreo(dto.getCorreo());
        Estudiante guardado = repositorioEstudiante.save(estudiante);
        return convertirADTO(guardado);
    }

    // Actualizar un estudiante existente
    public EstudianteDTO actualizar(Long id, EstudianteDTO dto) {
        Estudiante estudiante = repositorioEstudiante.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + id));

        // Verificar correo duplicado solo si cambió
        if (!estudiante.getCorreo().equals(dto.getCorreo())) {
            repositorioEstudiante.findByCorreo(dto.getCorreo()).ifPresent(e -> {
                throw new RuntimeException("Ya existe un estudiante con el correo: " + dto.getCorreo());
            });
        }

        estudiante.setNombre(dto.getNombre());
        estudiante.setApellido(dto.getApellido());
        estudiante.setCorreo(dto.getCorreo());
        Estudiante actualizado = repositorioEstudiante.save(estudiante);
        return convertirADTO(actualizado);
    }

    // Eliminar un estudiante (también elimina sus notas por CascadeType.ALL)
    @Transactional
    public void eliminar(Long id) {
        if (!repositorioEstudiante.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + id);
        }
        repositorioEstudiante.deleteById(id);
    }

    // Calcular nota final ponderada del estudiante
    public double calcularNotaFinal(Long id) {
        if (!repositorioEstudiante.existsById(id)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + id);
        }
        List<edu.javeriana.estudiante_notas.modelo.Nota> notas = repositorioNota.findByEstudianteId(id);
        if (notas.isEmpty()) {
            return 0.0;
        }
        double sumaPonderada = notas.stream()
                .mapToDouble(n -> n.getValor() * n.getPorcentaje() / 100.0)
                .sum();
        return Math.round(sumaPonderada * 100.0) / 100.0;
    }

    // Convertir entidad → DTO
    private EstudianteDTO convertirADTO(Estudiante estudiante) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getId());
        dto.setNombre(estudiante.getNombre());
        dto.setApellido(estudiante.getApellido());
        dto.setCorreo(estudiante.getCorreo());
        return dto;
    }
}
