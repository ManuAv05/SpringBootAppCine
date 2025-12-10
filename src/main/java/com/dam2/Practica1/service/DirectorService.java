package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Director;
import com.dam2.Practica1.dto.DirectorCreateUpdateDto;
import com.dam2.Practica1.dto.DirectorDto;
import com.dam2.Practica1.repository.DirectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Transactional(readOnly = true)
    public List<DirectorDto> listar() {
        return directorRepository.findAll().stream()
                .map(d -> DirectorDto.builder().id(d.getId()).nombre(d.getNombre()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public DirectorDto crear(DirectorCreateUpdateDto dto) {
        Director director = Director.builder().nombre(dto.getNombre()).build();
        director = directorRepository.save(director);
        return DirectorDto.builder().id(director.getId()).nombre(director.getNombre()).build();
    }

    @Transactional
    public DirectorDto actualizar(Long id, DirectorCreateUpdateDto dto) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Director no encontrado"));

        director.setNombre(dto.getNombre());

        director = directorRepository.save(director);
        return DirectorDto.builder().id(director.getId()).nombre(director.getNombre()).build();
    }

    public void eliminar(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new RuntimeException("Director no encontrado");
        }
        directorRepository.deleteById(id);
    }
}
