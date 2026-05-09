package edu.javeriana.estudiante_notas.service;

import edu.javeriana.estudiante_notas.dto.NotaDTO;
import edu.javeriana.estudiante_notas.modelo.Estudiante;
import edu.javeriana.estudiante_notas.modelo.Nota;
import edu.javeriana.estudiante_notas.repositorio.RepositorioEstudiante;
import edu.javeriana.estudiante_notas.repositorio.RepositorioNota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotaService {

    @Autowired
    private RepositorioNota repositorioNota;

    @Autowired
    private RepositorioEstudiante repositorioEstudiante;

    // Obtener todas las notas de un estudiante; valida existencia del estudiante.
    public List<NotaDTO> obtenerNotasPorEstudiante(Long estudianteId) {
        if (!repositorioEstudiante.existsById(estudianteId)) {
            throw new RuntimeException("Estudiante no encontrado con id: " + estudianteId);
        }
        return repositorioNota.findByEstudianteId(estudianteId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener nota por ID (lanza excepción si no existe)
    public NotaDTO obtenerPorId(Long id) {
        Nota nota = repositorioNota.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));
        return convertirADTO(nota);
    }

    // Crear una nota asociada a un estudiante existente
    public NotaDTO crear(Long estudianteId, NotaDTO dto) {
        Estudiante estudiante = repositorioEstudiante.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con id: " + estudianteId));

        Nota nota = new Nota();
        nota.setMateria(dto.getMateria());
        nota.setObservacion(dto.getObservacion() != null ? dto.getObservacion() : "");
        nota.setValor(dto.getValor());
        nota.setPorcentaje(dto.getPorcentaje());
        nota.setEstudiante(estudiante);

        Nota guardada = repositorioNota.save(nota);
        return convertirADTO(guardada);
    }

    // Actualizar una nota existente (no cambia el estudiante asociado)
    public NotaDTO actualizar(Long id, NotaDTO dto) {
        Nota nota = repositorioNota.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada con id: " + id));

        nota.setMateria(dto.getMateria());
        nota.setObservacion(dto.getObservacion() != null ? dto.getObservacion() : "");
        nota.setValor(dto.getValor());
        nota.setPorcentaje(dto.getPorcentaje());

        Nota actualizada = repositorioNota.save(nota);
        return convertirADTO(actualizada);
    }

    // Eliminar una nota por id
    public void eliminar(Long id) {
        if (!repositorioNota.existsById(id)) {
            throw new RuntimeException("Nota no encontrada con id: " + id);
        }
        repositorioNota.deleteById(id);
    }

    // Calcular promedio simple de un arreglo de notas (método utilitario)
    public double calcularPromedio(double[] notas) {
        if (notas == null || notas.length == 0) return 0.0;
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        return suma / notas.length;
    }

    // Convertir entidad → DTO
    private NotaDTO convertirADTO(Nota nota) {
        NotaDTO dto = new NotaDTO();
        dto.setId(nota.getId());
        dto.setMateria(nota.getMateria());
        dto.setObservacion(nota.getObservacion());
        dto.setValor(nota.getValor());
        dto.setPorcentaje(nota.getPorcentaje());
        return dto;
    }
}