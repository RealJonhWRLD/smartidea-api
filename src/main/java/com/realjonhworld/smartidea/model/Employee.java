package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Foto (Salvaremos a URL ou null para usar ícone padrão)
    private String photoUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) // CPF tem que ser único
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    private String role; // Cargo

    private String phone; // Telefone

    private LocalDate birthDate; // Aniversário

    private String location; // Cidade/Localização

    private String pixKey; // Chave Pix

    private String socialLinks; // LinkedIn/Instagram (Texto simples)

    private String password; // Senha para ele logar no futuro

    // O Salário usamos BigDecimal para dinheiro (nunca use Double!)
    private BigDecimal salary;
}