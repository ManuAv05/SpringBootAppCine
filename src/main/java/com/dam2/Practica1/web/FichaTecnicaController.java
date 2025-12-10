package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.FichaTecnicaCreateUpdateDto;
import com.dam2.Practica1.dto.FichaTecnicaDto;
import com.dam2.Practica1.service.FichaTecnicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fichas")
@RequiredArgsConstructor
public class FichaTecnicaController {
    private final FichaTecnicaService fichaService;

    @GetMapping
    public ResponseEntity<List<FichaTecnicaDto>> listar() {
        return ResponseEntity.ok(fichaService.listar());
    }
    @PostMapping
    public ResponseEntity<FichaTecnicaDto> crear(@Valid @RequestBody FichaTecnicaCreateUpdateDto dto) {
        return new ResponseEntity<>(fichaService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FichaTecnicaDto> actualizar(@PathVariable Long id, @Valid @RequestBody FichaTecnicaCreateUpdateDto dto) {
        return ResponseEntity.ok(fichaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        fichaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}