package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.dto.contract.ContractDTO;
import com.realjonhworld.smartidea.dto.contract.ContractRequestDTO;
import com.realjonhworld.smartidea.model.ContractStatus;
import com.realjonhworld.smartidea.model.Property;
import com.realjonhworld.smartidea.model.PropertyContract;
import com.realjonhworld.smartidea.model.Tenant;
import com.realjonhworld.smartidea.repository.PropertyContractRepository;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import com.realjonhworld.smartidea.repository.TenantRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contracts")
@CrossOrigin(origins = "*")
public class PropertyContractController {

    private final PropertyContractRepository contractRepository;
    private final PropertyRepository propertyRepository;
    private final TenantRepository tenantRepository;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PropertyContractController(PropertyContractRepository contractRepository,
                                      PropertyRepository propertyRepository,
                                      TenantRepository tenantRepository) {
        this.contractRepository = contractRepository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    // CRIAR CONTRATO
    @PostMapping
    public ContractDTO create(@RequestBody ContractRequestDTO r) {
        Property property = propertyRepository.findById(UUID.fromString(r.propertyId()))
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        Tenant tenant = tenantRepository.findById(UUID.fromString(r.tenantId()))
                .orElseThrow(() -> new RuntimeException("Inquilino não encontrado"));

        PropertyContract c = PropertyContract.builder()
                .property(property)
                .tenant(tenant)
                .rentValue(r.rentValue())
                .condoValue(r.condoValue())
                .depositValue(r.depositValue())
                .paymentDay(r.paymentDay())
                .monthsInContract(r.monthsInContract())
                .iptuStatus(r.iptuStatus())
                .notes(r.notes())
                .startDate(parseDate(r.startDate()))
                .endDate(parseDate(r.endDate()))
                .status(ContractStatus.ACTIVE)
                .build();

        PropertyContract saved = contractRepository.save(c);
        return toDTO(saved);
    }

    // ENCERRAR CONTRATO
    @PostMapping("/{id}/terminate")
    public ContractDTO terminate(@PathVariable UUID id,
                                 @RequestParam(required = false) String reason,
                                 @RequestParam(required = false) String endDate) {

        PropertyContract c = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        c.setStatus(ContractStatus.TERMINATED);
        c.setTerminationReason(reason);
        if (endDate != null && !endDate.isBlank()) {
            c.setEndDate(parseDate(endDate));
        } else {
            c.setEndDate(LocalDate.now());
        }

        PropertyContract saved = contractRepository.save(c);
        return toDTO(saved);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        return LocalDate.parse(value, DATE_FORMAT);
    }

    private ContractDTO toDTO(PropertyContract c) {
        return new ContractDTO(
                c.getId(),
                c.getProperty() != null ? c.getProperty().getId() : null,
                c.getTenant() != null ? c.getTenant().getId() : null,
                c.getTenant() != null ? c.getTenant().getName() : null,
                c.getRentValue(),
                c.getCondoValue(),
                c.getDepositValue(),
                c.getPaymentDay(),
                c.getMonthsInContract(),
                c.getIptuStatus(),
                c.getNotes(),
                c.getStartDate() != null ? c.getStartDate().format(DATE_FORMAT) : null,
                c.getEndDate() != null ? c.getEndDate().format(DATE_FORMAT) : null,
                c.getStatus() != null ? c.getStatus().name() : null,
                c.getTerminationReason()
        );
    }
}
