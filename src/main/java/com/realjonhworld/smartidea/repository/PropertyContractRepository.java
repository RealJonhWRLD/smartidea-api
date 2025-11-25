package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.PropertyContract;
import com.realjonhworld.smartidea.model.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyContractRepository extends JpaRepository<PropertyContract, UUID> {

    // 1 contrato ativo por imóvel
    Optional<PropertyContract> findByPropertyIdAndStatus(UUID propertyId, ContractStatus status);

    // histórico ordenado (mais recente primeiro)
    List<PropertyContract> findByPropertyIdOrderByStartDateDesc(UUID propertyId);
}