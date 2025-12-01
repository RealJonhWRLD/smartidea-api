package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.dto.tenant.TenantDTO;
import com.realjonhworld.smartidea.dto.tenant.TenantRequestDTO;
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
    public List<TenantDTO> list(@RequestParam(required = false) String name) {
        List<Tenant> tenants = (name != null && !name.isBlank())
                ? tenantRepository.findByNameContainingIgnoreCase(name)
                : tenantRepository.findAll();

        return tenants.stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public TenantDTO get(@PathVariable UUID id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino não encontrado"));
        return toDTO(tenant);
    }

    @PostMapping
    public TenantDTO create(@RequestBody TenantRequestDTO r) {
        Tenant t = new Tenant();
        apply(r, t);
        Tenant saved = tenantRepository.save(t);
        return toDTO(saved);
    }

    @PutMapping("/{id}")
    public TenantDTO update(@PathVariable UUID id,
                            @RequestBody TenantRequestDTO r) {
        Tenant t = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino não encontrado"));
        apply(r, t);
        Tenant saved = tenantRepository.save(t);
        return toDTO(saved);
    }

    // helpers

    private void apply(TenantRequestDTO r, Tenant t) {
        t.setName(r.name());
        t.setTenantType(r.tenantType());
        t.setTenantCpf(r.tenantCpf());
        t.setTenantRg(r.tenantRg());
        t.setTenantEmail(r.tenantEmail());
        t.setTenantPhone(r.tenantPhone());
        t.setTenantPhone2(r.tenantPhone2());
        t.setTenantSocial(r.tenantSocial());
        t.setTenantBirthDate(r.tenantBirthDate());
        t.setTenantMaritalStatus(r.tenantMaritalStatus());
        t.setTenantProfession(r.tenantProfession());
        t.setCompanyName(r.companyName());
        t.setCompanyCnpj(r.companyCnpj());
        t.setLegalRepName(r.legalRepName());
        t.setLegalRepCpf(r.legalRepCpf());
    }

    private TenantDTO toDTO(Tenant t) {
        return new TenantDTO(
                t.getId(),
                t.getName(),
                t.getTenantType(),
                t.getTenantCpf(),
                t.getTenantRg(),
                t.getTenantEmail(),
                t.getTenantPhone(),
                t.getTenantPhone2(),
                t.getTenantSocial(),
                t.getTenantBirthDate(),
                t.getTenantMaritalStatus(),
                t.getTenantProfession(),
                t.getCompanyName(),
                t.getCompanyCnpj(),
                t.getLegalRepName(),
                t.getLegalRepCpf()
        );
    }
}
