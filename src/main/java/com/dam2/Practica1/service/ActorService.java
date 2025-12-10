package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Actor;
import com.dam2.Practica1.dto.ActorCreateUpdateDto;
import com.dam2.Practica1.dto.ActorDto;
import com.dam2.Practica1.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;

    @Transactional(readOnly = true)
    public List<ActorDto> listar() {
        return actorRepository.findAll().stream()
                .map(a -> ActorDto.builder().id(a.getId()).nombre(a.getNombre()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ActorDto crear(ActorCreateUpdateDto dto) {
        Actor actor = Actor.builder().nombre(dto.getNombre()).build();
        actor = actorRepository.save(actor);
        return ActorDto.builder().id(actor.getId()).nombre(actor.getNombre()).build();
    }
    @Transactional
    public ActorDto actualizar(Long id, ActorCreateUpdateDto dto) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new RuntimeException("Actor no encontrado"));
        actor.setNombre(dto.getNombre());
        actor = actorRepository.save(actor);
        return ActorDto.builder().id(actor.getId()).nombre(actor.getNombre()).build();
    }

    public void eliminar(Long id) {
        if (!actorRepository.existsById(id)) throw new RuntimeException("Actor no encontrado");
        actorRepository.deleteById(id);
    }
}
