package com.realjonhworld.smartidea.dto.contract;

import java.util.UUID;

public record ContractHistoryItemDTO(
        UUID id,
        String tenantName,
        String startDate,
        String endDate,
        String rentValue,   // continua String (ex: "R$ 2.000,00")
        String status
) {
}
