package edu.javeriana.estudiante_notas.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotaDTO {

    private Long id;
    @NotBlank(message = "La materia es obligatoria")
    private String materia;
    @NotBlank (message = "La observacion es obligatoria")
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
