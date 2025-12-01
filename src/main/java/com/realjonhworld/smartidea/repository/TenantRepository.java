package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    List<Tenant> findByNameContainingIgnoreCase(String name);

    Optional<Tenant> findByName(String name);
}
