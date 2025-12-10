package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ficha_tecnica")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FichaTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String director;
    private int duracion;
    private String pais;
}