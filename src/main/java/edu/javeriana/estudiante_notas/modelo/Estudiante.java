package edu.javeriana.estudiante_notas.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Entidad Estudiante que representa a un alumno en la base de datos.
// Contiene información básica y la lista de notas asociadas.
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del estudiante (no nulo)
    @Column(nullable = false)
    private String nombre;

    // Apellido del estudiante (no nulo)
    @Column(nullable = false)
    private String apellido;

    // Correo único del estudiante (no nulo y único)
    @Column(nullable = false, unique = true)
    private String correo;

    // Relación con Nota: un estudiante tiene muchas notas.
    // CascadeType.ALL para propagar operaciones y orphanRemoval para eliminar notas huérfanas.
    @OneToMany (mappedBy = "estudiante", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
}
