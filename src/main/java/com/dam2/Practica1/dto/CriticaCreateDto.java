package com.dam2.Practica1.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriticaCreateDto {
    
    @NotBlank(message = "El comentario no puede estar vacío")
    private String comentario;

    @Min(0) @Max(10)
    private double nota;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long peliculaId;
}
