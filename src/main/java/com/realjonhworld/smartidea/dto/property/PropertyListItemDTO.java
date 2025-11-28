package com.realjonhworld.smartidea.dto.property;

import java.util.UUID;

public record PropertyListItemDTO(
        UUID id,
        String name,
        String description,
        String propertyType,
        String status,
        String currentTenant,
        String currentContractStartDate,
        String currentContractEndDate,
        Double lat,
        Double lng
) {}
