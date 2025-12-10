package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "idioma")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Idioma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "idiomas")
    private List<Pelicula> peliculas;
}
