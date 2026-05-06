package edu.javeriana.estudiante_notas.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data


public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false, unique = true)
    private String correo;

    @OneToMany (mappedBy = "estudiante", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
}
