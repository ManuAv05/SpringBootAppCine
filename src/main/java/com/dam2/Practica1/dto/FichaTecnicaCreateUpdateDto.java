package com.dam2.Practica1.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FichaTecnicaCreateUpdateDto {
    private String director;
    private int duracion;
    private String pais;
}
