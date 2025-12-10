package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.IdiomaCreateUpdateDto;
import com.dam2.Practica1.dto.IdiomaDto;
import com.dam2.Practica1.service.IdiomaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/idiomas")
@RequiredArgsConstructor
public class IdiomaController {
    private final IdiomaService idiomaService;

    @GetMapping
    public ResponseEntity<List<IdiomaDto>> listar() {
        return ResponseEntity.ok(idiomaService.listar());
    }

    @PostMapping
    public ResponseEntity<IdiomaDto> crear(@Valid @RequestBody IdiomaCreateUpdateDto dto) {
        return new ResponseEntity<>(idiomaService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<IdiomaDto> actualizar(@PathVariable Long id, @Valid @RequestBody IdiomaCreateUpdateDto dto) {
        return ResponseEntity.ok(idiomaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        idiomaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
