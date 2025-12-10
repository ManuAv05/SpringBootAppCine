package com.dam2.Practica1.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuradoDto {
    private Long id;
    private String username;
    private String email;
    private List<String> peliculasVotadasTitulos;
}