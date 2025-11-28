package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // --- DADOS BÁSICOS DO IMÓVEL ---
    private String name;           // Ex: "Nome do Endereço"
    private String propertyType;   // Ex: "Salas" / "Galpão" / "Casa"

    @Column(columnDefinition = "TEXT")
    private String description;    // Texto completo: rua, bairro, cidade etc.

    // --- CADASTROS / MATRÍCULAS ---
    private String matricula;      // matrícula do imóvel (cartório)
    private String cagece;         // conta água
    private String enel;           // conta energia

    private String lastRenovation; // último ano/mês de reforma (string por enquanto)

    // --- STATUS GERAL DO IMÓVEL ---
    // "Alugado" / "Disponível" etc. (status atual resumido)
    private String propertyStatus;

    // Status do IPTU (Pago / Pendente / Isento)
    private String iptuStatus;     // 👈 NOVO CAMPO

    // Observações gerais do imóvel (não do contrato)
    @Column(columnDefinition = "TEXT")
    private String notes;

    // --- LOCALIZAÇÃO ---
    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;
}
