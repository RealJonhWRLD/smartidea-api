package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Nome que aparece na tela (pessoa ou empresa)
    @Column(nullable = false)
    private String name;

    // Tipo de inquilino: "PF" / "PJ" (pode ser null por enquanto)
    private String tenantType;

    // --- DADOS PESSOAIS (PF) ---
    private String tenantCpf;
    private String tenantRg;
    private String tenantEmail;
    private String tenantPhone;   // telefone principal
    private String tenantPhone2;  // telefone secundário
    private String tenantSocial;  // @instagram, facebook etc.
    private String tenantBirthDate;   // string (ex: "10/10/1990")
    private String tenantMaritalStatus;
    private String tenantProfession;

    // --- DADOS EMPRESA (PJ) ---
    private String companyName;
    private String companyCnpj;
    private String legalRepName;
    private String legalRepCpf;
}
