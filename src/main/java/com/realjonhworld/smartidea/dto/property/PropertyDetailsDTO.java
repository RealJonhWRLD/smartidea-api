package com.realjonhworld.smartidea.dto.property;

import java.util.UUID;

/**
 * DTO para detalhes do imóvel (dados fixos).
 */

public record PropertyDetailsDTO(
        UUID id,
        String name,
        String propertyType,
        String description,

        // novos campos visíveis no modal
        String matricula,
        String cagece,
        String enel,
        String lastRenovation,
        String propertyStatus,
        String iptuStatus,
        String notes,

        Double lat,
        Double lng
) {}


