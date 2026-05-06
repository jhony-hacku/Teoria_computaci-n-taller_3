// java
package edu.javeriana.estudiante_notas.service;

import edu.javeriana.estudiante_notas.modelo.Nota;
import edu.javeriana.estudiante_notas.repositorio.RepositorioNota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotaService {

    @Autowired
    private RepositorioNota repositorioNota;

    public double calcularPromedio(double[] notas) {
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        return suma / notas.length;
    }

    public List<Double> obtenerNotasPorEstudiante(Long estudianteId) {
        return repositorioNota.findByEstudianteId(estudianteId)
                .stream()
                .map(Nota::getValor)
                .collect(Collectors.toList());
    }

}