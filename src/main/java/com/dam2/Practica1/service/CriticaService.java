package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Critica;
import com.dam2.Practica1.domain.Pelicula;
import com.dam2.Practica1.domain.Usuario;
import com.dam2.Practica1.dto.CriticaCreateUpdateDto;
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
    private final UsuarioRepository usuarioRepository;
    private final PeliculaRepository peliculaRepository;

    @Transactional(readOnly = true)
    public List<CriticaDto> listar() {
        return criticaRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CriticaDto crear(CriticaCreateUpdateDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pelicula pelicula = peliculaRepository.findById(dto.getPeliculaId())
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        Critica critica = Critica.builder()
                .comentario(dto.getComentario())
                .nota(dto.getNota())
                .fecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now()) // Si no pone fecha, ponemos hoy
                .usuario(usuario)
                .pelicula(pelicula)
                .build();

        critica = criticaRepository.save(critica);
        return mapEntityToDto(critica);
    }

    private CriticaDto mapEntityToDto(Critica c) {
        return CriticaDto.builder()
                .id(c.getId())
                .comentario(c.getComentario())
                .nota(c.getNota())
                .fecha(c.getFecha())
                .username(c.getUsuario().getUsername())
                .peliculaTitulo(c.getPelicula().getTitulo())
                .build();
    }
    @Transactional
    public CriticaDto actualizar(Long id, CriticaCreateUpdateDto dto) {
        Critica critica = criticaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crítica no encontrada"));

        critica.setComentario(dto.getComentario());
        critica.setNota(dto.getNota());
        if (dto.getFecha() != null) {
            critica.setFecha(dto.getFecha());
        }

        if (dto.getUsuarioId() != null) {
            Usuario u = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow();
            critica.setUsuario(u);
        }
        if (dto.getPeliculaId() != null) {
            Pelicula p = peliculaRepository.findById(dto.getPeliculaId()).orElseThrow();
            critica.setPelicula(p);
        }

        critica = criticaRepository.save(critica);
        return mapEntityToDto(critica);
    }

    public void eliminar(Long id) {
        if (!criticaRepository.existsById(id)) {
            throw new RuntimeException("Crítica no encontrada");
        }
        criticaRepository.deleteById(id);
    }
}