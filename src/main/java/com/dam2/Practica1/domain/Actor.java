package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany
    @JoinTable(
            name = "actor_pelicula",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "pelicula_id"))
    @ToString.Exclude
    @Builder.Default
    private List<Pelicula> peliculas = new ArrayList<>();

    public void addPelicula(Pelicula p) {
        if(peliculas == null) peliculas = new ArrayList<>();
        peliculas.add(p);
        p.getActors().add(this);
    }
}