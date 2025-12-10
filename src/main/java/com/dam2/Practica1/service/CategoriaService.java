package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Categoria;
import com.dam2.Practica1.dto.CategoriaCreateUpdateDto;
import com.dam2.Practica1.dto.CategoriaDto;
import com.dam2.Practica1.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDto> listar() {
        return categoriaRepository.findAll().stream()
                .map(c -> CategoriaDto.builder().id(c.getId()).nombre(c.getNombre()).url(c.getUrl()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaDto crear(CategoriaCreateUpdateDto dto) {
        Categoria categoria = Categoria.builder().nombre(dto.getNombre()).url(dto.getUrl()).build();
        categoria = categoriaRepository.save(categoria);
        return CategoriaDto.builder().id(categoria.getId()).nombre(categoria.getNombre()).url(categoria.getUrl()).build();
    }
    @Transactional
    public CategoriaDto actualizar(Long id, CategoriaCreateUpdateDto dto) {
        Categoria c = categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        c.setNombre(dto.getNombre());
        c.setUrl(dto.getUrl());
        c = categoriaRepository.save(c);
        return CategoriaDto.builder().id(c.getId()).nombre(c.getNombre()).url(c.getUrl()).build();
    }

    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) throw new RuntimeException("Categoría no encontrada");
        categoriaRepository.deleteById(id);
    }
}
