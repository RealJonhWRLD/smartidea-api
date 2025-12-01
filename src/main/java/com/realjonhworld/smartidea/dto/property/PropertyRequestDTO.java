package com.realjonhworld.smartidea.dto.property;

public record PropertyRequestDTO(
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
        Double lng
) {
}
