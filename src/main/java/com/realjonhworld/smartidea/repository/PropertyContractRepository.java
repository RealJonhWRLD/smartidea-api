package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.ContractStatus;
import com.realjonhworld.smartidea.model.PropertyContract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyContractRepository extends JpaRepository<PropertyContract, UUID> {

    List<PropertyContract> findByPropertyIdOrderByStartDateDesc(UUID propertyId);

    // NOVO: pegar o contrato ATIVO mais recente do imóvel
    Optional<PropertyContract> findFirstByPropertyIdAndStatusOrderByStartDateDesc(
            UUID propertyId,
            ContractStatus status
    );
}