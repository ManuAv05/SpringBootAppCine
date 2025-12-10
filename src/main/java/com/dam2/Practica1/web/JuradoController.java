package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.JuradoCreateUpdateDto;
import com.dam2.Practica1.dto.JuradoDto;
import com.dam2.Practica1.service.JuradoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jurados")
@RequiredArgsConstructor
public class JuradoController {
    private final JuradoService juradoService;

    @GetMapping
    public ResponseEntity<List<JuradoDto>> listar() {
        return ResponseEntity.ok(juradoService.listar());
    }

    @PostMapping
    public ResponseEntity<JuradoDto> crear(@Valid @RequestBody JuradoCreateUpdateDto dto) {
        return new ResponseEntity<>(juradoService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<JuradoDto> actualizar(@PathVariable Long id, @Valid @RequestBody JuradoCreateUpdateDto dto) {
        return ResponseEntity.ok(juradoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        juradoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
