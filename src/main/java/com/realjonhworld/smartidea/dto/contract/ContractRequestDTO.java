package com.realjonhworld.smartidea.dto.contract;

public record ContractRequestDTO(
        String propertyId,
        String tenantId,          // ou tenantId se você escolher pelo ID
        String rentValue,
        String condoValue,
        String depositValue,
        Integer paymentDay,
        Integer monthsInContract,
        String iptuStatus,
        String notes,
        String startDate,         // "dd/MM/yyyy"
        String endDate            // "dd/MM/yyyy" ou null
) {}
