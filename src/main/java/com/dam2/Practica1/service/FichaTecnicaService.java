package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.FichaTecnica;
import com.dam2.Practica1.dto.FichaTecnicaCreateUpdateDto;
import com.dam2.Practica1.dto.FichaTecnicaDto;
import com.dam2.Practica1.repository.FichaTecnicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FichaTecnicaService {

    private final FichaTecnicaRepository fichaRepository;

    @Transactional(readOnly = true)
    public List<FichaTecnicaDto> listar() {
        return fichaRepository.findAll().stream()
                .map(f -> FichaTecnicaDto.builder()
                        .id(f.getId())
                        .director(f.getDirector())
                        .duracion(f.getDuracion())
                        .pais(f.getPais())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public FichaTecnicaDto crear(FichaTecnicaCreateUpdateDto dto) {
        FichaTecnica ficha = FichaTecnica.builder()
                .director(dto.getDirector())
                .duracion(dto.getDuracion())
                .pais(dto.getPais())
                .build();
        ficha = fichaRepository.save(ficha);
        return FichaTecnicaDto.builder().id(ficha.getId()).director(ficha.getDirector()).duracion(ficha.getDuracion()).pais(ficha.getPais()).build();
    }
    @Transactional
    public FichaTecnicaDto actualizar(Long id, FichaTecnicaCreateUpdateDto dto) {
        FichaTecnica ficha = fichaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ficha técnica no encontrada"));

        ficha.setDirector(dto.getDirector());
        ficha.setDuracion(dto.getDuracion());
        ficha.setPais(dto.getPais());

        ficha = fichaRepository.save(ficha);

        return FichaTecnicaDto.builder()
                .id(ficha.getId())
                .director(ficha.getDirector())
                .duracion(ficha.getDuracion())
                .pais(ficha.getPais())
                .build();
    }

    public void eliminar(Long id) {
        if (!fichaRepository.existsById(id)) {
            throw new RuntimeException("Ficha técnica no encontrada");
        }
        fichaRepository.deleteById(id);
    }
}
