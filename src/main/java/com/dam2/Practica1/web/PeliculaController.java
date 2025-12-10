package com.dam2.Practica1.web;
import jakarta.validation.Valid;
import com.dam2.Practica1.dto.PeliculaCreateUpdateDto;
import com.dam2.Practica1.dto.PeliculaDto;
import com.dam2.Practica1.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/peliculas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class PeliculaController {

    private final PeliculaService peliculaService;

    @GetMapping
    public ResponseEntity<List<PeliculaDto>> listarTodas() {
        return ResponseEntity.ok(peliculaService.listarTodasDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeliculaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(peliculaService.buscarPorIdDto(id));
    }

    @PostMapping
    public ResponseEntity<PeliculaDto> crear(@Valid @RequestBody PeliculaCreateUpdateDto dto) {
        // Fíjate que he puesto @Valid antes de @RequestBody
        return new ResponseEntity<>(peliculaService.crearPelicula(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeliculaDto> actualizar(@PathVariable Long id, @Valid @RequestBody PeliculaCreateUpdateDto dto) {
        // Aquí también el @Valid
        return ResponseEntity.ok(peliculaService.actualizarPelicula(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        peliculaService.eliminarPelicula(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mejores/{puntuacion}")
    public ResponseEntity<List<PeliculaDto>> mejoresPeliculas(@PathVariable int puntuacion) {
        return ResponseEntity.ok(peliculaService.mejores_peliculas(puntuacion));
    }

    @PostMapping("/importar")
    public ResponseEntity<String> importarDatos() {
        try {
            peliculaService.importarCarpeta("datos");
            return ResponseEntity.ok("Importación iniciada en segundo plano...");
        } catch (IOException | URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al importar: " + e.getMessage());
        }
    }
    @PostMapping("/votar/{juradoId}")
    public ResponseEntity<String> votar(@PathVariable String juradoId) {
        peliculaService.votarJurado(juradoId);
        return ResponseEntity.ok("Voto registrado asíncronamente para el jurado: " + juradoId);
    }

    @GetMapping("/ranking")
    public ResponseEntity<Map<String, Integer>> verRanking() {
        return ResponseEntity.ok(peliculaService.getRanking());
    }
}