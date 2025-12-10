package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Critica;
import com.dam2.Practica1.domain.Pelicula;
import com.dam2.Practica1.domain.Usuario;
import com.dam2.Practica1.dto.CriticaCreateDto;
import com.dam2.Practica1.dto.CriticaDto;
import com.dam2.Practica1.repository.CriticaRepository;
import com.dam2.Practica1.repository.PeliculaRepository;
import com.dam2.Practica1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CriticaService {

    private final CriticaRepository criticaRepository;
    private final PeliculaRepository peliculaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<CriticaDto> listarPorPelicula(Long peliculaId) {
        return criticaRepository.findByPeliculaId(peliculaId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CriticaDto crear(CriticaCreateDto dto) {
        Pelicula pelicula = peliculaRepository.findById(dto.getPeliculaId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));
        
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Critica critica = Critica.builder()
                .comentario(dto.getComentario())
                .nota(dto.getNota())
                .fecha(LocalDate.now())
                .pelicula(pelicula)
                .usuario(usuario)
                .build();
        
        critica = criticaRepository.save(critica);
        return mapToDto(critica);
    }

    private CriticaDto mapToDto(Critica c) {
        return CriticaDto.builder()
                .id(c.getId())
                .comentario(c.getComentario())
                .nota(c.getNota())
                .fecha(c.getFecha())
                .usuario(c.getUsuario().getUsername())
                .peliculaId(c.getPelicula().getId())
                .build();
    }
}