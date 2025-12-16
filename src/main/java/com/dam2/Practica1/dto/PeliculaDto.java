package com.dam2.Practica1.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PeliculaDto {
    private Long id;
    private String titulo;
    private int duracion;
    private LocalDate fechaEstreno;
    private String sinopsis;
    private int valoracion;
    private String urlImagen;

    private String nombreDirector;
    private Long directorId;

    private List<String> nombresActores;
    private List<Long> actorIds;

    private List<String> nombresCategorias;
    private List<Long> categoriaIds;

    private List<String> nombresIdiomas;
    private List<Long> idiomaIds;
}