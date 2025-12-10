package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Jurado;
import com.dam2.Practica1.domain.Usuario;
import com.dam2.Practica1.dto.JuradoCreateUpdateDto;
import com.dam2.Practica1.dto.JuradoDto;
import com.dam2.Practica1.repository.JuradoRepository;
import com.dam2.Practica1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuradoService {

    private final JuradoRepository juradoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<JuradoDto> listar() {
        return juradoRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public JuradoDto crear(JuradoCreateUpdateDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (juradoRepository.findAll().stream().anyMatch(j -> j.getUsuario().getId().equals(usuario.getId()))) {
            throw new RuntimeException("Este usuario ya es jurado");
        }

        Jurado jurado = Jurado.builder()
                .usuario(usuario)
                .peliculasVotadas(new ArrayList<>())
                .build();

        jurado = juradoRepository.save(jurado);
        return mapEntityToDto(jurado);
    }

    private JuradoDto mapEntityToDto(Jurado j) {
        return JuradoDto.builder()
                .id(j.getId())
                .username(j.getUsuario().getUsername())
                .email(j.getUsuario().getEmail())
                .peliculasVotadasTitulos(
                        j.getPeliculasVotadas().stream()
                                .map(p -> p.getTitulo())
                                .collect(Collectors.toList())
                )
                .build();
    }
    @Transactional
    public JuradoDto actualizar(Long id, JuradoCreateUpdateDto dto) {
        Jurado jurado = juradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jurado no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        jurado.setUsuario(usuario);

        jurado = juradoRepository.save(jurado);
        return mapEntityToDto(jurado);
    }

    public void eliminar(Long id) {
        if (!juradoRepository.existsById(id)) {
            throw new RuntimeException("Jurado no encontrado");
        }
        juradoRepository.deleteById(id);
    }
}
