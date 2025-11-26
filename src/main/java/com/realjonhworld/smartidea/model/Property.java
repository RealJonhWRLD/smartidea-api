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

    // --- DADOS BÁSICOS ---
    private String name;           // Endereço Principal
    private String propertyType;   // Casa, Comercial, etc.
    @Column(columnDefinition = "TEXT")
    private String description;    // Detalhes / Complemento

    // --- CLIENTE ---
    private String clientName;     // Inquilino
    private String clientPhone;    // Telefone do Cliente (NOVO)

    // --- DADOS DO IMÓVEL ---
    private String matricula;
    private String cagece;         // Medidor Cagece (NOVO)
    private String enel;           // Medidor Enel (NOVO)
    private String lastRenovation; // Última Reforma (NOVO)
    private String propertyStatus; // Alugado ou Disponível (NOVO)

    // --- FINANCEIRO ---
    private String rentValue;      // Valor Aluguel
    private String condoValue;     // Valor Condomínio (NOVO)
    private String depositValue;   // Valor Calção (NOVO)
    private String iptuStatus;     // Status IPTU

    // --- CONTRATO ---
    private String rentDueDate;       // Dia Vencimento
    private String contractStartDate;    // Início Contrato (DD/MM/AAAA)
    private String contractDueDate;   // Vencimento Contrato
    private String contractMonths;    // Nº Meses Contrato (NOVO)
    private String rentPaymentStatus; // Status Pagamento (Em dia/Atrasado)

    // exemplos típicos de campos no backend
    private String tenantType;          // PF ou PJ
    private String tenantCpf;
    private String tenantRg;
    private String tenantEmail;
    private String tenantPhone2;
    private String tenantSocial;
    private String tenantBirthDate;
    private String tenantMaritalStatus;
    private String tenantProfession;

    // empresa
    private String companyName;
    private String companyCnpj;
    private String legalRepName;
    private String legalRepCpf;

    // --- EXTRAS ---
    @Column(columnDefinition = "TEXT")
    private String notes;          // Observação (NOVO)

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;
}