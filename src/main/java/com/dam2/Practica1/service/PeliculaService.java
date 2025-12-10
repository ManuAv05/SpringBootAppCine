package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.*;
import com.dam2.Practica1.dto.PeliculaCreateUpdateDto;
import com.dam2.Practica1.dto.PeliculaDto;
import com.dam2.Practica1.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PeliculaService {

    private final PeliculaRepository peliculaRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;
    private final CategoriaRepository categoriaRepository;
    private final IdiomaRepository idiomaRepository;
    private final FichaTecnicaRepository fichaTecnicaRepository;

    private final List<String> candidatos = List.of("Interstellar", "The Dark Knight", "Soul", "Oppenheimer", "El club de la Lucha");
    private final Map<String, Integer> resultadosVotacion = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private static final Semaphore VOTO_SEMAPHORE = new Semaphore(5);

    @Transactional
    public PeliculaDto crearPelicula(PeliculaCreateUpdateDto dto) {
        Pelicula pelicula = new Pelicula();
        pelicula.setActors(new ArrayList<>());
        pelicula.setCategorias(new ArrayList<>());
        pelicula.setIdiomas(new ArrayList<>());

        mapDtoToEntity(dto, pelicula);
        pelicula = peliculaRepository.save(pelicula);
        return mapEntityToDto(pelicula);
    }

    @Transactional
    public PeliculaDto actualizarPelicula(Long id, PeliculaCreateUpdateDto dto) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + id));

        mapDtoToEntity(dto, pelicula);
        pelicula = peliculaRepository.save(pelicula);
        return mapEntityToDto(pelicula);
    }

    @Transactional(readOnly = true)
    public PeliculaDto buscarPorIdDto(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        return mapEntityToDto(pelicula);
    }

    public Pelicula buscarEntidadPorId(Long id) {
        return peliculaRepository.findById(id).orElse(null);
    }

    @Transactional
    public void eliminarPelicula(Long id) {
        Pelicula pelicula = peliculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar, ID no existe"));

        if (pelicula.getActors() != null) {
            for (Actor actor : pelicula.getActors()) {
                actor.getPeliculas().remove(pelicula);
            }
            pelicula.getActors().clear();
        }

        if (pelicula.getJuradosQueVotaron() != null) {
            for (Jurado jurado : pelicula.getJuradosQueVotaron()) {
                jurado.getPeliculasVotadas().remove(pelicula);
            }
            pelicula.getJuradosQueVotaron().clear();
        }

        peliculaRepository.delete(pelicula);
    }

    @Transactional(readOnly = true)
    public List<PeliculaDto> listarTodasDto() {
        return peliculaRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    public List<PeliculaDto> mejores_peliculas(int valoracion){
        return peliculaRepository.findAll().stream()
                .filter(p -> p.getValoracion() >= valoracion)
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------
    // MÉTODOS DE MAPEO (AQUÍ ESTÁN LOS CAMBIOS)
    // -------------------------------------------------------------------

    private void mapDtoToEntity(PeliculaCreateUpdateDto dto, Pelicula pelicula) {
        pelicula.setTitulo(dto.getTitulo());
        pelicula.setDuracion(dto.getDuracion());
        pelicula.setFechaEstreno(dto.getFechaEstreno());
        pelicula.setSinopsis(dto.getSinopsis());
        pelicula.setValoracion(dto.getValoracion());

        // --- CAMBIO: AÑADIDO SET DE IMAGEN ---
        pelicula.setUrlImagen(dto.getUrlImagen());
        // -------------------------------------

        if (dto.getDirectorId() != null) {
            Director director = directorRepository.findById(dto.getDirectorId())
                    .orElseThrow(() -> new RuntimeException("Director no encontrado"));
            pelicula.setDirector(director);
        }

        if (dto.getActorIds() != null && !dto.getActorIds().isEmpty()) {
            List<Actor> actores = actorRepository.findAllById(dto.getActorIds());
            pelicula.setActors(actores);
        }

        if (dto.getCategoriaIds() != null && !dto.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
            pelicula.setCategorias(categorias);
        }

        if (dto.getIdiomaIds() != null && !dto.getIdiomaIds().isEmpty()) {
            List<Idioma> idiomas = idiomaRepository.findAllById(dto.getIdiomaIds());
            pelicula.setIdiomas(idiomas);
        }

        if (dto.getFichaTecnicaId() != null) {
            FichaTecnica ficha = fichaTecnicaRepository.findById(dto.getFichaTecnicaId())
                    .orElse(null);
            pelicula.setFichaTecnica(ficha);
        }
    }

    private PeliculaDto mapEntityToDto(Pelicula p) {
        PeliculaDto dto = new PeliculaDto();

        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setDuracion(p.getDuracion());
        dto.setFechaEstreno(p.getFechaEstreno());
        dto.setSinopsis(p.getSinopsis());
        dto.setValoracion(p.getValoracion());

        // --- CAMBIO: AÑADIDO GET DE IMAGEN ---
        dto.setUrlImagen(p.getUrlImagen());
        // -------------------------------------

        if (p.getDirector() != null) {
            dto.setNombreDirector(p.getDirector().getNombre());
        }

        if (p.getActors() != null) {
            dto.setNombresActores(p.getActors().stream()
                    .map(Actor::getNombre)
                    .collect(Collectors.toList()));
        }

        if (p.getCategorias() != null) {
            dto.setNombresCategorias(p.getCategorias().stream()
                    .map(Categoria::getNombre)
                    .collect(Collectors.toList()));
        }

        if (p.getIdiomas() != null) {
            dto.setNombresIdiomas(p.getIdiomas().stream()
                    .map(Idioma::getNombre)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // -------------------------------------------------------------------

    public String tareaLenta(String titulo) {
        try {
            System.out.println("Iniciando tarea para " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000);
            System.out.println("Terminando tarea para " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Procesada " + titulo;
    }

    @Async("taskExecutor")
    public CompletableFuture<String> tareaLenta2(String titulo) {
        try {
            System.out.println("Iniciando " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000);
            System.out.println("Terminando " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Procesada " + titulo);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> reproducir(String titulo) {
        try {
            System.out.println("Reproduciendo " + titulo + " en " + Thread.currentThread().getName());
            Thread.sleep(3000);
            System.out.println("Terminando " + titulo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Procesada " + titulo);
    }

    public void importarCarpeta(String rutaCarpeta) throws IOException, URISyntaxException {
        long inicio = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        var resource = getClass().getClassLoader().getResource("datos");
        if(resource == null) {
            System.err.println("La carpeta 'datos' no se encuentra en resources.");
            return;
        }

        Path carpeta = Paths.get(resource.toURI());

        try (Stream<Path> paths = Files.list(carpeta)) {
            paths.filter(Files::isRegularFile).forEach(path -> {
                String nombre = path.toString().toLowerCase();
                if (nombre.endsWith(".csv") || nombre.endsWith(".txt")) {
                    futures.add(importarCsvAsync(path));
                } else if (nombre.endsWith(".xml")) {
                    futures.add(importarXmlAsync(path));
                }
            });
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        long fin = System.currentTimeMillis();
        System.out.println("Importación completa en " + (fin - inicio) + " ms");
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> importarCsvAsync(Path fichero) {
        try {
            System.out.println("Procesando CSV: " + fichero + " en " + Thread.currentThread().getName());

            List<Pelicula> lista = new ArrayList<>();
            List<String> lineas = Files.readAllLines(fichero);
            if(!lineas.isEmpty()) lineas.remove(0);

            for (String linea : lineas) {
                String[] campos = linea.split(";");
                Pelicula p = Pelicula.builder()
                        .titulo(campos[0])
                        .duracion(Integer.parseInt(campos[1]))
                        .fechaEstreno(LocalDate.parse(campos[2]))
                        .sinopsis(campos[3])
                        .actors(new ArrayList<>())
                        .categorias(new ArrayList<>())
                        .idiomas(new ArrayList<>())
                        .build();
                lista.add(p);
            }

            peliculaRepository.saveAll(lista);
            System.out.println("Finalizado CSV: " + fichero);

        } catch (Exception e) {
            System.err.println("Error en CSV " + fichero + ": " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> importarXmlAsync(Path fichero) {
        try {
            System.out.println("Procesando XML: " + fichero + " en " + Thread.currentThread().getName());
            List<Pelicula> lista = new ArrayList<>();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fichero.toFile());
            NodeList nodos = doc.getElementsByTagName("pelicula");

            for (int i = 0; i < nodos.getLength(); i++) {
                Element e = (Element) nodos.item(i);
                Pelicula p = Pelicula.builder()
                        .titulo(e.getElementsByTagName("titulo").item(0).getTextContent())
                        .duracion(Integer.parseInt(e.getElementsByTagName("duracion").item(0).getTextContent()))
                        .fechaEstreno(LocalDate.parse(e.getElementsByTagName("fechaEstreno").item(0).getTextContent()))
                        .sinopsis(e.getElementsByTagName("sinopsis").item(0).getTextContent())
                        .actors(new ArrayList<>())
                        .categorias(new ArrayList<>())
                        .idiomas(new ArrayList<>())
                        .build();
                lista.add(p);
            }

            peliculaRepository.saveAll(lista);
            System.out.println("Finalizado XML: " + fichero);

        } catch (Exception e) {
            System.err.println("Error en XML " + fichero + ": " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> votarJurado(String juradoId) {
        try {
            VOTO_SEMAPHORE.acquire();

            Thread.sleep(random.nextInt(100) + 50);

            String peliculaVotada = candidatos.get(random.nextInt(candidatos.size()));
            int puntos = random.nextInt(11);

            System.out.println(String.format("Jurado %s (Hilos activos: %d/5) votando: %s recibe %d puntos",
                    juradoId, 5 - VOTO_SEMAPHORE.availablePermits(), peliculaVotada, puntos));

            resultadosVotacion.compute(peliculaVotada, (key, value) -> (value == null) ? puntos : value + puntos);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            VOTO_SEMAPHORE.release();
        }
        return CompletableFuture.completedFuture(null);
    }

    public Map<String, Integer> getRanking() {
        return resultadosVotacion.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public void limpiarResultados() {
        this.resultadosVotacion.clear();
    }
}