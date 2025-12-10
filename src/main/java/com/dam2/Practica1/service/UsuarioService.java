package com.dam2.Practica1.service;

import com.dam2.Practica1.domain.Usuario;
import com.dam2.Practica1.dto.UsuarioCreateUpdateDto;
import com.dam2.Practica1.dto.UsuarioDto;
import com.dam2.Practica1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll().stream()
                .map(u -> UsuarioDto.builder().id(u.getId()).username(u.getUsername()).email(u.getEmail()).rol(u.getRol()).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioDto crear(UsuarioCreateUpdateDto dto) {
        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();
        usuario = usuarioRepository.save(usuario);
        return UsuarioDto.builder().id(usuario.getId()).username(usuario.getUsername()).email(usuario.getEmail()).rol(usuario.getRol()).build();
    }
    @Transactional
    public UsuarioDto actualizar(Long id, UsuarioCreateUpdateDto dto) {
        Usuario u = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setRol(dto.getRol());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(dto.getPassword());
        }
        u = usuarioRepository.save(u);
        return UsuarioDto.builder().id(u.getId()).username(u.getUsername()).email(u.getEmail()).rol(u.getRol()).build();
    }

    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) throw new RuntimeException("Usuario no encontrado");
        usuarioRepository.deleteById(id);
    }
}