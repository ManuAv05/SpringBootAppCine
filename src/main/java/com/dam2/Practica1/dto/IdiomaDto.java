package com.dam2.Practica1.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdiomaDto {
    private Long id;
    private String nombre;
}