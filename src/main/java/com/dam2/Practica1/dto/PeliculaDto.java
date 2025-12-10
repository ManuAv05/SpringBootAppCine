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
    private List<String> nombresActores;
    private List<String> nombresCategorias;
    private List<String> nombresIdiomas;
}