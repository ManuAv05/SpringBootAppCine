package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Idioma;
import com.dam2.Practica1.dto.IdiomaCreateUpdateDto;
import com.dam2.Practica1.dto.IdiomaDto;
import com.dam2.Practica1.repository.IdiomaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdiomaService {

    private final IdiomaRepository idiomaRepository;

    @Transactional(readOnly = true)
    public List<IdiomaDto> listar() {
        return idiomaRepository.findAll().stream()
                .map(i -> IdiomaDto.builder().id(i.getId()).nombre(i.getNombre()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public IdiomaDto crear(IdiomaCreateUpdateDto dto) {
        Idioma idioma = Idioma.builder().nombre(dto.getNombre()).build();
        idioma = idiomaRepository.save(idioma);
        return IdiomaDto.builder().id(idioma.getId()).nombre(idioma.getNombre()).build();
    }
    @Transactional
    public IdiomaDto actualizar(Long id, IdiomaCreateUpdateDto dto) {
        Idioma i = idiomaRepository.findById(id).orElseThrow(() -> new RuntimeException("Idioma no encontrado"));
        i.setNombre(dto.getNombre());
        i = idiomaRepository.save(i);
        return IdiomaDto.builder().id(i.getId()).nombre(i.getNombre()).build();
    }

    public void eliminar(Long id) {
        if (!idiomaRepository.existsById(id)) throw new RuntimeException("Idioma no encontrado");
        idiomaRepository.deleteById(id);
    }
}
