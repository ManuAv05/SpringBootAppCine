package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.CategoriaCreateUpdateDto;
import com.dam2.Practica1.dto.CategoriaDto;
import com.dam2.Practica1.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDto>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @PostMapping
    public ResponseEntity<CategoriaDto> crear(@Valid @RequestBody CategoriaCreateUpdateDto dto) {
        return new ResponseEntity<>(categoriaService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDto> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaCreateUpdateDto dto) {
        return ResponseEntity.ok(categoriaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
