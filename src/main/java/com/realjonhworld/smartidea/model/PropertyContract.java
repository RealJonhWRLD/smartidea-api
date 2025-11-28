package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // --- RELACIONAMENTOS ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    // --- DATAS ---

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;

    // --- VALORES ---

    private String rentValue;      // ex: "R$ 750,00"
    private String condoValue;
    private String depositValue;

    // Dia de vencimento do aluguel (ex: "05")
    private String paymentDay;

    private Integer monthsInContract;

    // Status do IPTU (ex: "Pago", "Em aberto")
    private String iptuStatus;

    // Observações do contrato
    @Column(columnDefinition = "TEXT")
    private String notes;

    // Status do contrato (ATIVO, TERMINATED, RESCINDED...)
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    // Motivo de rescisão (opcional)
    private String terminationReason;
}
