package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.ContractStatus;
import com.realjonhworld.smartidea.model.PropertyContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyContractRepository extends JpaRepository<PropertyContract, UUID> {

    // histórico de contratos de um imóvel (ordenado do mais recente para o mais antigo)
    List<PropertyContract> findByPropertyIdOrderByStartDateDesc(UUID propertyId);

    // contrato ATIVO mais recente de um imóvel (usado na sidebar)
    Optional<PropertyContract> findFirstByPropertyIdAndStatusOrderByStartDateDesc(
            UUID propertyId,
            ContractStatus status
    );
}
