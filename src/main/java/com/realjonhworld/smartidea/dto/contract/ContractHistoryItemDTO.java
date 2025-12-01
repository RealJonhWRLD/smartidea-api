package com.realjonhworld.smartidea.dto.contract;

import java.util.UUID;

public record ContractHistoryItemDTO(
        UUID id,
        String tenantName,
        String startDate,
        String endDate,
        Double rentValue,
        String status
) {}

