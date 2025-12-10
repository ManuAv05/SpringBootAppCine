package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "director")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "director")
    @ToString.Exclude
    @Builder.Default
    private List<Pelicula> peliculas = new ArrayList<>();
}