package edu.javeriana.estudiante_notas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Nota {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private long id;
    @Column (nullable = false)
    private String materia;
    @Column(nullable = false)
    private String observacion;
    @Column(nullable = false)
    private double valor;
    @Column(nullable = false)
    private Double porcentaje;

    @ManyToOne (optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    @JsonIgnore
    private Estudiante estudiante;
}
