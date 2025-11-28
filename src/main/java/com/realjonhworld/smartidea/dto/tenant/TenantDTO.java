package com.realjonhworld.smartidea.dto.tenant;

import java.util.UUID;

/**
 * DTO para exibir dados de inquilino/cliente.
 */
public record TenantDTO(
        UUID id,
        String name,
        String tenantType,        // "PF" ou "PJ" (pode ser null)
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
) {
}
