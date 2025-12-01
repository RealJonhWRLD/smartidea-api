package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Nome do cliente
    private String name;

    // "PF" ou "PJ"
    private String tenantType;

    private String tenantCpf;
    private String tenantRg;
    private String tenantEmail;
    private String tenantPhone;
    private String tenantPhone2;

    private String tenantSocial;
    private String tenantBirthDate;
    private String tenantMaritalStatus;
    private String tenantProfession;

    // Dados de empresa (se for PJ)
    private String companyName;
    private String companyCnpj;
    private String legalRepName;
    private String legalRepCpf;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyContract> contracts;
}
