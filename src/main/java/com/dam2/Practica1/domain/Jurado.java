package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "jurado")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Jurado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
            name = "voto_jurado_pelicula",
            joinColumns = @JoinColumn(name = "jurado_id"),
            inverseJoinColumns = @JoinColumn(name = "pelicula_id")
    )
    private List<Pelicula> peliculasVotadas;
}