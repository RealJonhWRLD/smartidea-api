package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.dto.contract.ContractHistoryItemDTO;
import com.realjonhworld.smartidea.dto.property.PropertyDetailsDTO;
import com.realjonhworld.smartidea.dto.property.PropertyListItemDTO;
import com.realjonhworld.smartidea.dto.property.PropertyUpdateRequest;
import com.realjonhworld.smartidea.model.ContractStatus;
import com.realjonhworld.smartidea.model.Property;
import com.realjonhworld.smartidea.model.PropertyContract;
import com.realjonhworld.smartidea.repository.PropertyContractRepository;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyRepository propertyRepository;
    private final PropertyContractRepository propertyContractRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PropertyController(
            PropertyRepository propertyRepository,
            PropertyContractRepository propertyContractRepository
    ) {
        this.propertyRepository = propertyRepository;
        this.propertyContractRepository = propertyContractRepository;
    }

    // 1) LISTA (mapa / sidebar)
    @GetMapping
    public List<PropertyListItemDTO> listProperties() {

        List<Property> properties = propertyRepository.findAll();

        return properties.stream()
                .map(property -> {

                    PropertyContract active = propertyContractRepository
                            .findFirstByPropertyIdAndStatusOrderByStartDateDesc(
                                    property.getId(),
                                    ContractStatus.ACTIVE
                            )
                            .orElse(null);

                    String status = (active != null) ? "ALUGADO" : "DISPONIVEL";
                    String tenantName = (active != null) ? active.getTenant().getName() : null;

                    String startDate = (active != null && active.getStartDate() != null)
                            ? active.getStartDate().format(DATE_FORMAT)
                            : null;

                    String endDate = (active != null && active.getEndDate() != null)
                            ? active.getEndDate().format(DATE_FORMAT)
                            : null;

                    return new PropertyListItemDTO(
                            property.getId(),
                            property.getName(),
                            property.getDescription(),
                            property.getPropertyType(),
                            status,
                            tenantName,
                            startDate,
                            endDate,
                            property.getLat(),
                            property.getLng()
                    );
                })
                .toList();
    }

    // 2) DETALHES COMPLETOS DO IMÓVEL (usado pelo modal)
    @GetMapping("/{id}")
    public PropertyDetailsDTO getPropertyDetails(@PathVariable UUID id) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        return new PropertyDetailsDTO(
                property.getId(),
                property.getName(),
                property.getPropertyType(),
                property.getDescription(),

                property.getMatricula(),
                property.getCagece(),
                property.getEnel(),
                property.getLastRenovation(),
                property.getPropertyStatus(),
                property.getIptuStatus(),
                property.getNotes(),

                property.getLat(),
                property.getLng()
        );
    }

    // 3) HISTÓRICO DE CONTRATOS
    @GetMapping("/{id}/contracts")
    public List<ContractHistoryItemDTO> getContractsByProperty(@PathVariable UUID id) {

        List<PropertyContract> contracts =
                propertyContractRepository.findByPropertyIdOrderByStartDateDesc(id);

        return contracts.stream()
                .map(contract -> new ContractHistoryItemDTO(
                        contract.getId(),
                        contract.getTenant().getName(),
                        contract.getStartDate() != null
                                ? contract.getStartDate().format(DATE_FORMAT)
                                : null,
                        contract.getEndDate() != null
                                ? contract.getEndDate().format(DATE_FORMAT)
                                : null,
                        contract.getRentValue(),
                        contract.getStatus().name()
                ))
                .toList();
    }

    // 4) ATUALIZAR IMÓVEL
    @PutMapping("/{id}")
    public PropertyDetailsDTO updateProperty(
            @PathVariable UUID id,
            @RequestBody PropertyUpdateRequest request
    ) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        if (request.name() != null) {
            property.setName(request.name());
        }
        if (request.propertyType() != null) {
            property.setPropertyType(request.propertyType());
        }
        if (request.description() != null) {
            property.setDescription(request.description());
        }
        if (request.matricula() != null) {
            property.setMatricula(request.matricula());
        }
        if (request.cagece() != null) {
            property.setCagece(request.cagece());
        }
        if (request.enel() != null) {
            property.setEnel(request.enel());
        }
        if (request.lastRenovation() != null) {
            property.setLastRenovation(request.lastRenovation());
        }
        if (request.propertyStatus() != null) {
            property.setPropertyStatus(request.propertyStatus());
        }
        if (request.iptuStatus() != null) {
            property.setIptuStatus(request.iptuStatus());
        }
        if (request.notes() != null) {
            property.setNotes(request.notes());
        }
        if (request.lat() != null) {
            property.setLat(request.lat());
        }
        if (request.lng() != null) {
            property.setLng(request.lng());
        }

        Property saved = propertyRepository.save(property);

        return new PropertyDetailsDTO(
                saved.getId(),
                saved.getName(),
                saved.getPropertyType(),
                saved.getDescription(),

                saved.getMatricula(),
                saved.getCagece(),
                saved.getEnel(),
                saved.getLastRenovation(),
                saved.getPropertyStatus(),
                saved.getIptuStatus(),
                saved.getNotes(),

                saved.getLat(),
                saved.getLng()
        );
    }
}
