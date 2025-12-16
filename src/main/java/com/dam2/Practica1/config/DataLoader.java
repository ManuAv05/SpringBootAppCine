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
            System.out.println(">>> CHECKING/LOADING DATA...");

            // 1. Categories
            Categoria sciFi = createCategoriaIfNotFound(categoriaRepo, "Ciencia Ficción", "http://scifi.com");
            Categoria drama = createCategoriaIfNotFound(categoriaRepo, "Drama", "http://drama.com");
            Categoria animacion = createCategoriaIfNotFound(categoriaRepo, "Animación", "http://pixar.com");
            Categoria terror = createCategoriaIfNotFound(categoriaRepo, "Terror", "http://terror.com");
            Categoria thriller = createCategoriaIfNotFound(categoriaRepo, "Thriller", "http://thriller.com");

            // 2. Languages
            Idioma ingles = createIdiomaIfNotFound(idiomaRepo, "Inglés");
            Idioma espanol = createIdiomaIfNotFound(idiomaRepo, "Español");

            // 3. Movies
            // Soul
            Director docter = createDirectorIfNotFound(directorRepo, "Pete Docter");
            createPeliculaIfNotFound(peliculaRepo, "Soul", 100, LocalDate.of(2020, 12, 25),
                    "Un músico busca su propósito tras un accidente.", 8,
                    "Pete Docter", "EE.UU.", docter,
                    "https://lumiere-a.akamaihd.net/v1/images/p_soul_disneyplus_v2_20907_764da65d.jpeg",
                    List.of(animacion, drama), List.of(ingles, espanol));

            // Interstellar
            Director nolan = createDirectorIfNotFound(directorRepo, "Christopher Nolan");
            createPeliculaIfNotFound(peliculaRepo, "Interstellar", 169, LocalDate.of(2014, 11, 7),
                    "Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento de asegurar la supervivencia de la humanidad.", 9,
                    "Christopher Nolan", "EE.UU.", nolan,
                    "https://imusic.b-cdn.net/images/item/original/935/5051892182935.jpg?interstellar-2015-interstellar-dvd&class=scaled&v=1691753575",
                    List.of(sciFi, drama), List.of(ingles));

            // The Shining (El Resplandor)
            Director kubrick = createDirectorIfNotFound(directorRepo, "Stanley Kubrick");
            createPeliculaIfNotFound(peliculaRepo, "The Shining", 146, LocalDate.of(1980, 5, 23),
                    "Una familia se dirige a un hotel aislado para pasar el invierno. Allí, una presencia espiritual maligna influye en el padre hacia la violencia.", 9,
                    "Stanley Kubrick", "EE.UU.", kubrick,
                    "https://m.media-amazon.com/images/M/MV5BYmUxZDU3NjktMzA1OS00OGUwLWJkOTctYzhjOGI5MTcyY2U3XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                    List.of(terror, thriller), List.of(ingles, espanol));

            // Se7en
            Director fincher = createDirectorIfNotFound(directorRepo, "David Fincher");
            createPeliculaIfNotFound(peliculaRepo, "Se7en", 127, LocalDate.of(1995, 9, 22),
                    "Dos detectives siguen la pista de un asesino en serie cuyos crímenes se basan en los siete pecados capitales.", 9,
                    "David Fincher", "EE.UU.", fincher,
                    "https://i.pinimg.com/736x/8a/53/32/8a53323e0c5108d210a7d1a6658b4cc9.jpg",
                    List.of(thriller, drama), List.of(ingles));

            // Get Out (Déjame salir)
            Director peele = createDirectorIfNotFound(directorRepo, "Jordan Peele");
            createPeliculaIfNotFound(peliculaRepo, "Get Out", 104, LocalDate.of(2017, 2, 24),
                    "Un joven afroamericano visita la propiedad de la familia blanca de su novia, donde descubre un oscuro secreto.", 8,
                    "Jordan Peele", "EE.UU.", peele,
                    "https://www.ecartelera.com/images/noticias/fotos/38000/38080/1.jpg",
                    List.of(terror, thriller), List.of(ingles));

            // As Above, So Below (Así en la Tierra como en el Infierno)
            Director dowdle = createDirectorIfNotFound(directorRepo, "John Erick Dowdle");
            createPeliculaIfNotFound(peliculaRepo, "Así en la Tierra como en el Infierno", 93, LocalDate.of(2014, 8, 29),
                    "Un equipo de exploradores se ventura en las catacumbas de París, descubriendo el oscuro secreto que yace en la ciudad de los muertos.", 7,
                    "John Erick Dowdle", "EE.UU.", dowdle,
                    "https://m.media-amazon.com/images/M/MV5BYTlkMTVkNTQtYjUxOC00NDRkLWEwNmEtYzVmNmUwM2ZiMjJkXkEyXkFqcGc@._V1_.jpg",
                    List.of(terror, thriller), List.of(ingles, espanol));

            // The Conjuring (Expediente Warren)
            Director wan = createDirectorIfNotFound(directorRepo, "James Wan");
            createPeliculaIfNotFound(peliculaRepo, "Expediente Warren: The Conjuring", 112, LocalDate.of(2013, 7, 19),
                    "Los investigadores paranormales Ed y Lorraine Warren ayudan a una familia aterrorizada por una presencia oscura en su granja.", 8,
                    "James Wan", "EE.UU.", wan,
                    "https://m.media-amazon.com/images/M/MV5BNDk0MjYzNzctZWRhNi00NmVjLTgxYTctNjE2OGYwOWI1NDgxXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
                    List.of(terror, thriller), List.of(ingles, espanol));

            // Hereditary
            Director aster = createDirectorIfNotFound(directorRepo, "Ari Aster");
            createPeliculaIfNotFound(peliculaRepo, "Hereditary", 127, LocalDate.of(2018, 6, 8),
                    "Tras la muerte de la matriarca, una familia se ve acechada por una presencia oscura y trágica que desentraña secretos aterradores.", 9,
                    "Ari Aster", "EE.UU.", aster,
                    "https://es.web.img2.acsta.net/pictures/18/06/06/10/24/2467257.jpg",
                    List.of(terror, drama), List.of(ingles));

            // Sinister
            Director derrickson = createDirectorIfNotFound(directorRepo, "Scott Derrickson");
            createPeliculaIfNotFound(peliculaRepo, "Sinister", 110, LocalDate.of(2012, 10, 12),
                    "Un escritor de crímenes reales encuentra una caja de películas caseras que sugieren que el asesinato que está investigando es obra de un asesino en serie sobrenatural.", 7,
                    "Scott Derrickson", "EE.UU.", derrickson,
                    "https://m.media-amazon.com/images/I/81GVVDwM-aL._AC_UF894,1000_QL80_.jpg",
                    List.of(terror, thriller), List.of(ingles));

            // 4. Admin User
            if (usuarioRepo.findByUsername("admin").isEmpty()) {
                Usuario admin = Usuario.builder()
                        .username("admin")
                        .password("1234")
                        .email("admin@cine.com")
                        .rol("ADMIN")
                        .build();
                usuarioRepo.save(admin);
            }

            System.out.println(">>> CHECKING/LOADING DATA COMPLETED");
        };
    }

    private Categoria createCategoriaIfNotFound(CategoriaRepository repo, String nombre, String url) {
        // Simple check (assuming findAll is small enough for this sample or we'd add findByNombre)
        return repo.findAll().stream().filter(c -> c.getNombre().equals(nombre)).findFirst()
                .orElseGet(() -> repo.save(Categoria.builder().nombre(nombre).url(url).build()));
    }

    private Idioma createIdiomaIfNotFound(IdiomaRepository repo, String nombre) {
        return repo.findAll().stream().filter(i -> i.getNombre().equals(nombre)).findFirst()
                .orElseGet(() -> repo.save(Idioma.builder().nombre(nombre).build()));
    }

    private Director createDirectorIfNotFound(DirectorRepository repo, String nombre) {
        return repo.findAll().stream().filter(d -> d.getNombre().equals(nombre)).findFirst()
                .orElseGet(() -> repo.save(Director.builder().nombre(nombre).peliculas(new ArrayList<>()).build()));
    }

    private void createPeliculaIfNotFound(PeliculaRepository repo, String titulo, int duracion, LocalDate estreno,
                                          String sinopsis, int valoracion, String directorFicha, String pais,
                                          Director directorEntity, String urlImagen,
                                          List<Categoria> categorias, List<Idioma> idiomas) {
        
        if (repo.findByTitulo(titulo).isEmpty()) {
            FichaTecnica ficha = FichaTecnica.builder().director(directorFicha).duracion(duracion).pais(pais).build();
            Pelicula p = Pelicula.builder()
                    .titulo(titulo)
                    .duracion(duracion)
                    .fechaEstreno(estreno)
                    .sinopsis(sinopsis)
                    .valoracion(valoracion)
                    .fichaTecnica(ficha)
                    .director(directorEntity)
                    .urlImagen(urlImagen)
                    .categorias(new ArrayList<>(categorias))
                    .idiomas(new ArrayList<>(idiomas))
                    .build();
            repo.save(p);
            // Link director
            directorEntity.getPeliculas().add(p);
        } else {
             // Update image always to ensure revisions are applied
             repo.findByTitulo(titulo).ifPresent(p -> {
                 p.setUrlImagen(urlImagen);
                 repo.save(p);
             });
        }
    }
}