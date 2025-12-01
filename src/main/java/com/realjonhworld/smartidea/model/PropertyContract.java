package com.realjonhworld.smartidea.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "property_contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyContract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    // Valores como texto (pra bater com o front atual)
    private String rentValue;
    private String condoValue;
    private String depositValue;

    // Dia do vencimento do aluguel (1..31)
    private Integer paymentDay;

    // Meses de contrato
    private Integer monthsInContract;

    private String iptuStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    // Motivo de rescisão
    private String terminationReason;
}
