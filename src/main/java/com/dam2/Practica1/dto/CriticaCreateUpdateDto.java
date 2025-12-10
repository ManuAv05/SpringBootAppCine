package com.dam2.Practica1.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriticaCreateUpdateDto {
    private String comentario;
    private double nota;
    private LocalDate fecha;

    private Long usuarioId;
    private Long peliculaId;
}