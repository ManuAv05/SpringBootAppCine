package com.dam2.Practica1.config;

import com.dam2.Practica1.domain.*;
import com.dam2.Practica1.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(ActorRepository actorRepo,
                               DirectorRepository directorRepo,
                               FichaTecnicaRepository fichaRepo,
                               PeliculaRepository peliculaRepo,
                               CategoriaRepository categoriaRepo,
                               IdiomaRepository idiomaRepo,
                               UsuarioRepository usuarioRepo) {

        return args -> {

            if (peliculaRepo.count() > 0) {
                System.out.println(">>> DATOS DE PRUEBA YA CARGADOS. Omitiendo carga inicial.");
                return;
            }

            System.out.println(">>> CARGANDO DATOS DE PRUEBA ACTUALIZADOS...");

            Categoria sciFi = Categoria.builder().nombre("Ciencia Ficción").url("http://scifi.com").build();
            Categoria drama = Categoria.builder().nombre("Drama").url("http://drama.com").build();
            Categoria animacion = Categoria.builder().nombre("Animación").url("http://pixar.com").build();

            categoriaRepo.saveAll(List.of(sciFi, drama, animacion));

            Idioma ingles = Idioma.builder().nombre("Inglés").build();
            Idioma espanol = Idioma.builder().nombre("Español").build();

            idiomaRepo.saveAll(List.of(ingles, espanol));

            Director nolan = Director.builder().nombre("Christopher Nolan").peliculas(new ArrayList<>()).build();
            Director docter = Director.builder().nombre("Pete Docter").peliculas(new ArrayList<>()).build();

            directorRepo.saveAll(List.of(nolan, docter));

            FichaTecnica f1 = FichaTecnica.builder().director("Christopher Nolan").duracion(169).pais("EE.UU.").build();
            FichaTecnica f2 = FichaTecnica.builder().director("Pete Docter").duracion(100).pais("EE.UU.").build();


            Pelicula interstellar = Pelicula.builder()
                    .titulo("Interstellar")
                    .duracion(169)
                    .fechaEstreno(LocalDate.of(2014, 11, 7))
                    .sinopsis("Viaje a través de un agujero de gusano.")
                    .valoracion(9)
                    .fichaTecnica(f1) // Asignamos la ficha (aún sin ID)
                    .director(nolan)
                    .categorias(new ArrayList<>(List.of(sciFi, drama)))
                    .idiomas(new ArrayList<>(List.of(ingles)))
                    .build();

            Pelicula soul = Pelicula.builder()
                    .titulo("Soul")
                    .duracion(100)
                    .fechaEstreno(LocalDate.of(2020, 12, 25))
                    .sinopsis("Un músico busca su propósito.")
                    .valoracion(8)
                    .fichaTecnica(f2) // Asignamos la ficha (aún sin ID)
                    .director(docter)
                    .categorias(new ArrayList<>(List.of(animacion, drama)))
                    .idiomas(new ArrayList<>(List.of(ingles, espanol)))
                    .build();

            peliculaRepo.saveAll(List.of(interstellar, soul));

            Actor matthew = Actor.builder().nombre("Matthew McConaughey").peliculas(new ArrayList<>()).build();
            Actor hathaway = Actor.builder().nombre("Anne Hathaway").peliculas(new ArrayList<>()).build();
            Actor foxx = Actor.builder().nombre("Jamie Foxx").peliculas(new ArrayList<>()).build();

            matthew.addPelicula(interstellar);
            hathaway.addPelicula(interstellar);
            foxx.addPelicula(soul);

            actorRepo.saveAll(List.of(matthew, hathaway, foxx));

            Usuario admin = Usuario.builder()
                    .username("admin")
                    .password("1234")
                    .email("admin@cine.com")
                    .rol("ADMIN")
                    .build();

            usuarioRepo.save(admin);

            System.out.println(">>> DATOS DE PRUEBA INSERTADOS CORRECTAMENTE");
        };
    }
}