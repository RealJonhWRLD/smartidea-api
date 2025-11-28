package com.realjonhworld.smartidea.dto.tenant;

/**
 * DTO para criação/edição de inquilino.
 * Você pode usar o mesmo formato de campos do TenantDTO
 * ou simplificar conforme seu front.
 */
public record TenantCreateRequest(
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
) {
}
