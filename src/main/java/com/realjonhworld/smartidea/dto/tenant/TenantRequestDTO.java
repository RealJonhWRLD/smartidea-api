package com.realjonhworld.smartidea.dto.tenant;

public record TenantRequestDTO(
        String name,
        String tenantType,
        String tenantCpf,
        String tenantRg,
        String tenantEmail,
        String tenantPhone,
        String tenantPhone2,
        String tenantSocial,
        String tenantBirthDate,
        String tenantMaritalStatus,
        String tenantProfession,
        String companyName,
        String companyCnpj,
        String legalRepName,
        String legalRepCpf
) {}

