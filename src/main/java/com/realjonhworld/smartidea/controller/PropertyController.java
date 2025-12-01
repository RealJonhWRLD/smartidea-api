package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.dto.contract.ContractHistoryItemDTO;
import com.realjonhworld.smartidea.dto.property.PropertyDTO;
import com.realjonhworld.smartidea.dto.property.PropertyRequestDTO;
import com.realjonhworld.smartidea.model.ContractStatus;
import com.realjonhworld.smartidea.model.Property;
import com.realjonhworld.smartidea.model.PropertyContract;
import com.realjonhworld.smartidea.repository.PropertyContractRepository;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyRepository propertyRepository;
    private final PropertyContractRepository contractRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PropertyController(
            PropertyRepository propertyRepository,
            PropertyContractRepository contractRepository
    ) {
        this.propertyRepository = propertyRepository;
        this.contractRepository = contractRepository;
    }

    // ========================================================================
    // LISTA DE IMÓVEIS (tabela + mapa)
    // ========================================================================
    @GetMapping
    public List<PropertyDTO> list() {
        return propertyRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    // ========================================================================
    // DETALHES DE UM IMÓVEL (modal de edição)
    // ========================================================================
    @GetMapping("/{id}")
    public PropertyDTO get(@PathVariable UUID id) {
        Property p = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        return toDTO(p);
    }

    // ========================================================================
    // CRIAR IMÓVEL
    // ========================================================================
    @PostMapping
    public PropertyDTO create(@RequestBody PropertyRequestDTO request) {
        Property property = new Property();
        applyRequest(request, property);
        Property saved = propertyRepository.save(property);
        return toDTO(saved);
    }

    // ========================================================================
    // EDITAR IMÓVEL
    // ========================================================================
    @PutMapping("/{id}")
    public PropertyDTO update(@PathVariable UUID id,
                              @RequestBody PropertyRequestDTO request) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        applyRequest(request, property);
        Property saved = propertyRepository.save(property);
        return toDTO(saved);
    }

    // ========================================================================
    // HISTÓRICO DE CONTRATOS DE UM IMÓVEL
    // ========================================================================
    @GetMapping("/{id}/contracts")
    public List<ContractHistoryItemDTO> getContracts(@PathVariable UUID id) {
        List<PropertyContract> contracts =
                contractRepository.findByPropertyIdOrderByStartDateDesc(id);

        return contracts.stream()
                .map(c -> {
                    String tenantName =
                            c.getTenant() != null ? c.getTenant().getName() : null;

                    String startDate = c.getStartDate() != null
                            ? c.getStartDate().format(DATE_FORMAT)
                            : null;

                    String endDate = c.getEndDate() != null
                            ? c.getEndDate().format(DATE_FORMAT)
                            : null;

                    // rentValue é String na entidade, mantemos String no DTO
                    String rentValue = c.getRentValue();

                    String status = c.getStatus() != null
                            ? c.getStatus().name()
                            : null;

                    return new ContractHistoryItemDTO(
                            c.getId(),
                            tenantName,
                            startDate,
                            endDate,
                            rentValue,
                            status
                    );
                })
                .toList();
    }

    // ========================================================================
    // HELPERS
    // ========================================================================

    private void applyRequest(PropertyRequestDTO r, Property p) {
        p.setName(r.name());
        p.setPropertyType(r.propertyType());
        p.setDescription(r.description());
        p.setMatricula(r.matricula());
        p.setCagece(r.cagece());
        p.setEnel(r.enel());
        p.setLastRenovation(r.lastRenovation());
        p.setPropertyStatus(r.propertyStatus());
        p.setIptuStatus(r.iptuStatus());
        p.setNotes(r.notes());
        p.setLat(r.lat());
        p.setLng(r.lng());
    }

    private PropertyDTO toDTO(Property p) {

        // contrato ATIVO mais recente desse imóvel (se existir)
        Optional<PropertyContract> optionalActive = contractRepository
                .findFirstByPropertyIdAndStatusOrderByStartDateDesc(
                        p.getId(),
                        ContractStatus.ACTIVE
                );

        String currentTenant = null;
        String currentStartDate = null;
        String currentEndDate = null;
        String currentRentValue = null;
        String currentContractStatus = null;

        if (optionalActive.isPresent()) {
            PropertyContract active = optionalActive.get();

            if (active.getTenant() != null) {
                currentTenant = active.getTenant().getName();
            }
            if (active.getStartDate() != null) {
                currentStartDate = active.getStartDate().format(DATE_FORMAT);
            }
            if (active.getEndDate() != null) {
                currentEndDate = active.getEndDate().format(DATE_FORMAT);
            }
            // rentValue vem como String da entidade
            currentRentValue = active.getRentValue();

            if (active.getStatus() != null) {
                currentContractStatus = active.getStatus().name();
            }
        }

        return new PropertyDTO(
                p.getId(),
                p.getName(),
                p.getPropertyType(),
                p.getDescription(),
                p.getMatricula(),
                p.getCagece(),
                p.getEnel(),
                p.getLastRenovation(),
                p.getPropertyStatus(),
                p.getIptuStatus(),
                p.getNotes(),
                p.getLat(),
                p.getLng(),
                currentTenant,
                currentStartDate,
                currentEndDate,
                currentRentValue,
                currentContractStatus
        );
    }
}
