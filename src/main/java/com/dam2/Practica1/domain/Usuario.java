package com.dam2.Practica1.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Jurado jurado;
}
