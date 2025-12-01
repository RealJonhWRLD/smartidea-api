package com.realjonhworld.smartidea.dto.property;

import java.util.UUID;

public record PropertyDTO(
        UUID id,
        String name,
        String propertyType,
        String description,
        String matricula,
        String cagece,
        String enel,
        String lastRenovation,
        String propertyStatus,
        String iptuStatus,
        String notes,
        Double lat,
        Double lng,

        // CAMPOS DO CONTRATO ATIVO
        String currentTenant,
        String currentContractStartDate,
        String currentContractEndDate,
        Double currentRentValue,
        String currentContractStatus
) {}
