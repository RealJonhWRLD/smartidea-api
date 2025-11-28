package com.realjonhworld.smartidea.dto.contract;

import java.util.UUID;

/**
 * DTO genérico de contrato para listagens gerais em /api/contracts.
 */
public record ContractDTO(
        UUID id,
        UUID propertyId,
        String propertyName,
        UUID tenantId,
        String tenantName,
        String startDate,
        String endDate,
        String rentValue,
        String status
) {
}
