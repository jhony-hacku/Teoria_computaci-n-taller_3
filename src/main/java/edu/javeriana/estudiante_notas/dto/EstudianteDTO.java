package edu.javeriana.estudiante_notas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO para transferir datos de Estudiante entre capas (sin relaciones).
@Data
public class EstudianteDTO {

    // Identificador (puede ser nulo al crear)
    private Long id;

    // Campos con validaciones para entrada de usuario
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email
    private String correo;
}
