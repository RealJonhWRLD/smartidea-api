package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Ex: "Galpão EMISA 1101"
    private String name;

    // Ex: "Casa", "Sala", "Galpão"
    private String propertyType;

    // Endereço completo / detalhes
    @Column(columnDefinition = "TEXT")
    private String description;

    // Matrícula do imóvel
    private String matricula;

    // Códigos de medidor
    private String cagece;
    private String enel;

    // Data da última reforma (texto livre por enquanto)
    private String lastRenovation;

    // Situação do imóvel (Disponível, Vendido, Alugado, etc.)
    private String propertyStatus;

    // Status IPTU (Pago / Em aberto / Isento)
    private String iptuStatus;

    // Observações gerais
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Geo
    private Double lat;
    private Double lng;

    // Histórico de contratos
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyContract> contracts;
}
