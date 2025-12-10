package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.DirectorCreateUpdateDto;
import com.dam2.Practica1.dto.DirectorDto;
import com.dam2.Practica1.service.DirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/directores")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public ResponseEntity<List<DirectorDto>> listar() {
        return ResponseEntity.ok(directorService.listar());
    }

    @PostMapping
    public ResponseEntity<DirectorDto> crear(@Valid @RequestBody DirectorCreateUpdateDto dto) {
        return new ResponseEntity<>(directorService.crear(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DirectorDto> actualizar(@PathVariable Long id, @Valid @RequestBody DirectorCreateUpdateDto dto) {
        return ResponseEntity.ok(directorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        directorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}