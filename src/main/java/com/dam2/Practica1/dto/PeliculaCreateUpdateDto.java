package com.dam2.Practica1.dto;

import jakarta.validation.constraints.*; // Importante importar esto
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeliculaCreateUpdateDto {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 2, max = 120, message = "El título debe tener entre 2 y 120 caracteres")
    private String titulo;

    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    private int duracion;

    @PastOrPresent(message = "La fecha de estreno no puede ser futura") // O @Past
    private LocalDate fechaEstreno;

    @Size(max = 255, message = "La sinopsis es demasiado larga")
    private String sinopsis;

    @Min(value = 0, message = "La valoración mínima es 0")
    @Max(value = 10, message = "La valoración máxima es 10")
    private int valoracion;

    private String urlImagen;

    // Los IDs no suelen validarse con estas anotaciones, se validan al buscar en BD
    private Long directorId;
    private Long fichaTecnicaId;
    private List<Long> actorIds;
    private List<Long> categoriaIds;
    private List<Long> idiomaIds;
}