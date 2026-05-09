package edu.javeriana.estudiante_notas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO para transferir información de Nota entre capas.
// Incluye validaciones sobre rango de valores y porcentaje.
@Data
public class NotaDTO {

    private Long id;

    // Id del estudiante al que pertenece la nota (debe ser proporcionado en creación)
    private Long estudianteId;

    @NotBlank(message = "La materia es obligatoria")
    private String materia;

    private String observacion;

    @NotNull (message = "El valor es obligatorio")
    @Min(value = 0, message = "El valor debe ser mayor o igual a 0")
    @Max(value = 5, message = "El valor debe ser menor o igual a 5")
    private Double valor;

    @NotNull (message = "El porcentaje es obligatorio")
    @Min(value = 0, message = "El porcentaje debe ser mayor o igual a 0")
    @Max(value = 100, message = "El porcentaje debe ser menor o igual a 100")
    private Double porcentaje;
}
