package com.dam2.Practica1.repository;

import com.dam2.Practica1.domain.Critica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticaRepository extends JpaRepository<Critica, Long> {
    // Al extender de JpaRepository, heredas automáticamente findAll, save, delete, etc.
}