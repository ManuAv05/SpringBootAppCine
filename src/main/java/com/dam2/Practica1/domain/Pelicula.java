package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "peliculas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, length = 120)
    private String titulo;

    private int duracion;

    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    private String sinopsis;
    private int valoracion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ficha_id")
    private FichaTecnica fichaTecnica;

    @ManyToOne
    @JoinColumn(name = "director_id", nullable = true)
    private Director director;

    @Column(length = 500) // Le damos espacio por si la URL es larga
    private String urlImagen;

    @Builder.Default
    @ManyToMany(mappedBy = "peliculas")
    @ToString.Exclude
    private List<Actor> actors = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "pelicula_categoria",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @ToString.Exclude
    private List<Categoria> categorias = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "pelicula_idioma",
            joinColumns = @JoinColumn(name = "pelicula_id"),
            inverseJoinColumns = @JoinColumn(name = "idioma_id")
    )
    @ToString.Exclude
    private List<Idioma> idiomas = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Critica> criticas = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "peliculasVotadas")
    @ToString.Exclude
    private List<Jurado> juradosQueVotaron = new ArrayList<>();
}