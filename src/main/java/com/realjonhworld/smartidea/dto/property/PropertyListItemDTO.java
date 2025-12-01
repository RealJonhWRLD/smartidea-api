package com.realjonhworld.smartidea.dto.property;

public record PropertyListItemDTO(
        java.util.UUID id,
        String name,
        String propertyType,
        String tenantName,     // inquilino atual (se tiver)
        String matricula,
        String rentDueDate,    // venc. aluguel (se tiver contrato ativo)
        String contractEndDate,
        String rentValue,
        String paymentStatus   // "Em dia", "Atrasado", etc. (por enquanto pode vir null)
) {}
