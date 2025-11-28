package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    // usado pelo PropertyContractController para buscar/criar inquilino
    Optional<Tenant> findByName(String name);

    // usado pelo TenantController para listar com filtro por nome
    List<Tenant> findByNameContainingIgnoreCase(String name);
}
