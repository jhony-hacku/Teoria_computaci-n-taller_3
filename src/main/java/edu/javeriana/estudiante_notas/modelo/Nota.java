package edu.javeriana.estudiante_notas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad Nota que representa una calificación asociada a un estudiante.
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Nota {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;

    // Materia evaluada (no nulo)
    @Column (nullable = false)
    private String materia;

    // Observación opcional sobre la nota
    @Column(nullable = false)
    private String observacion;

    // Valor de la nota (0-5)
    @Column(nullable = false)
    private double valor;

    // Porcentaje de ponderación de la nota (0-100)
    @Column(nullable = false)
    private Double porcentaje;

    // Relación ManyToOne con Estudiante. @JsonIgnore para evitar ciclos en serialización JSON.
    @ManyToOne (optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @JsonIgnore
    private Estudiante estudiante;
}
