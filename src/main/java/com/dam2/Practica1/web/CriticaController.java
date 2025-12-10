package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.CriticaCreateDto;
import com.dam2.Practica1.dto.CriticaDto;
import com.dam2.Practica1.service.CriticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/criticas")
@RequiredArgsConstructor
public class CriticaController {

    private final CriticaService criticaService;

    @GetMapping("/pelicula/{id}")
    public ResponseEntity<List<CriticaDto>> listarPorPelicula(@PathVariable Long id) {
        return ResponseEntity.ok(criticaService.listarPorPelicula(id));
    }

    @PostMapping
    public ResponseEntity<CriticaDto> crear(@Valid @RequestBody CriticaCreateDto dto) {
        return new ResponseEntity<>(criticaService.crear(dto), HttpStatus.CREATED);
    }
}
