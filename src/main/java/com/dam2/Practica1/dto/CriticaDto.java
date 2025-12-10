package com.dam2.Practica1.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriticaDto {
    private Long id;
    private String comentario;
    private double nota;
    private LocalDate fecha;

    private String username;
    private String peliculaTitulo;
}