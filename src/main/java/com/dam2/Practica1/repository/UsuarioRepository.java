package com.dam2.Practica1.repository;

import com.dam2.Practica1.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Método extra útil para el login
    Optional<Usuario> findByUsername(String username);
}