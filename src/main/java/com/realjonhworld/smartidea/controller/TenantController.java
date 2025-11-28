package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.model.Tenant;
import com.realjonhworld.smartidea.repository.TenantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
@CrossOrigin(origins = "*")

public class TenantController {

    private final TenantRepository tenantRepository;

    public TenantController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    public List<Tenant> listTenants(@RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return tenantRepository.findByNameContainingIgnoreCase(name);
        }
        return tenantRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tenant getTenant(@PathVariable UUID id) {
        return tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino não encontrado"));
    }

    @PostMapping
    public Tenant createTenant(@RequestBody Tenant tenant) {
        return tenantRepository.save(tenant);
    }
}
