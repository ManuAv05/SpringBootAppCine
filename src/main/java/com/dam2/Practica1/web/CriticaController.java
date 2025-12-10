package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.CriticaCreateUpdateDto;
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

    @GetMapping
    public ResponseEntity<List<CriticaDto>> listar() {
        return ResponseEntity.ok(criticaService.listar());
    }

    @PostMapping
    public ResponseEntity<CriticaDto> crear(@Valid @RequestBody CriticaCreateUpdateDto dto) {
        return new ResponseEntity<>(criticaService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CriticaDto> actualizar(@PathVariable Long id, @Valid @RequestBody CriticaCreateUpdateDto dto) {
        return ResponseEntity.ok(criticaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        criticaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
