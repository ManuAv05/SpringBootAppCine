package com.dam2.Practica1.web;

import com.dam2.Practica1.dto.ActorCreateUpdateDto;
import com.dam2.Practica1.dto.ActorDto;
import com.dam2.Practica1.service.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actores")
@RequiredArgsConstructor
public class ActorController {
    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorDto>> listar() {
        return ResponseEntity.ok(actorService.listar());
    }

    @PostMapping
    public ResponseEntity<ActorDto> crear(@Valid @RequestBody ActorCreateUpdateDto dto) {
        return new ResponseEntity<>(actorService.crear(dto), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ActorDto> actualizar(@PathVariable Long id, @Valid @RequestBody ActorCreateUpdateDto dto) {
        return ResponseEntity.ok(actorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
