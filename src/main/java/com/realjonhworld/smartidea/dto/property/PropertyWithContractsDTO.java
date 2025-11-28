package com.realjonhworld.smartidea.dto.property;

import com.realjonhworld.smartidea.dto.contract.ContractHistoryItemDTO;

import java.util.List;

/**
 * DTO que junta detalhes do imóvel + histórico de contratos.
 * Útil se quiser um endpoint /properties/{id}/full.
 */
public record PropertyWithContractsDTO(
        PropertyDetailsDTO property,
        List<ContractHistoryItemDTO> contractsHistory
) {
}
