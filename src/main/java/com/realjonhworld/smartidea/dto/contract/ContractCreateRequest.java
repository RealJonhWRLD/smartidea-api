package com.realjonhworld.smartidea.dto.contract;

public record ContractCreateRequest(
        String propertyId,
        String tenantName,
        String rentValue,
        String condoValue,
        String depositValue,
        String paymentDay,
        Integer monthsInContract,
        String iptuStatus,
        String notes,
        String startDate,
        String endDate
) {}
