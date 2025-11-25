package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "property_contracts")
public class PropertyContract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Imóvel dono do contrato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    // Inquilino / Cliente no momento desse contrato
    private String tenantName;
    private String tenantPhone;

    // Datas
    private LocalDate startDate;   // início do contrato
    private LocalDate endDate;     // fim (planejado ou rescisão)

    // Valores
    private String rentValue;      // seguimos como String "R$ 1.500,00" pra bater com front
    private String depositValue;   // caução
    private String condoValue;     // condomínio

    private String paymentDay;     // ex "05", "10"
    private Integer monthsInContract; // meses planejados / efetivos
    private Integer monthsLate;       // meses em atraso (se quiser controlar aqui)

    // Status do contrato
    @Enumerated(EnumType.STRING)
    private ContractStatus status;    // ACTIVE, TERMINATED

    private String terminationReason; // motivo rescisão opcional

    private LocalDate createdAt;
}