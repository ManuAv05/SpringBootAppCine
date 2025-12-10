package com.dam2.Practica1.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorDto {
    private Long id;
    private String nombre;
    private List<String> peliculasTitulos;
}