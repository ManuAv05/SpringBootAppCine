package com.dam2.Practica1.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaCreateUpdateDto {
    private String nombre;
    private String url;
}