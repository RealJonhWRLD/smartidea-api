package com.realjonhworld.smartidea.dto.contract;

import java.util.UUID;

/**
 * DTO para representar um item do histórico de contratos de um imóvel.
 */
public record ContractHistoryItemDTO(
        UUID id,
        String tenantName,
        String startDate,   // dd/MM/yyyy
        String endDate,     // dd/MM/yyyy ou null
        String rentValue,   // ex: "R$ 750,00"
        String status       // "ATIVO", "ENCERRADO", "RESCINDIDO"
) {
}
