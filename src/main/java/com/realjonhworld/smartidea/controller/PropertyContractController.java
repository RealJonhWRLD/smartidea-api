package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.dto.contract.ContractCreateRequest;
import com.realjonhworld.smartidea.dto.contract.ContractHistoryItemDTO;
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
import java.util.List;
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

    public PropertyContractController(
            PropertyContractRepository contractRepository,
            PropertyRepository propertyRepository,
            TenantRepository tenantRepository
    ) {
        this.contractRepository = contractRepository;
        this.propertyRepository = propertyRepository;
        this.tenantRepository = tenantRepository;
    }

    // 1) CRIAR NOVO CONTRATO
    @PostMapping
    public ContractHistoryItemDTO createContract(@RequestBody ContractCreateRequest req) {

        // ---- valida e converte propertyId (String -> UUID) ----
        if (req.propertyId() == null || req.propertyId().isBlank()) {
            throw new RuntimeException("propertyId não informado no corpo da requisição");
        }

        UUID propertyUuid = UUID.fromString(req.propertyId());

        Property property = propertyRepository.findById(propertyUuid)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        // ---- busca / cria inquilino por NOME ----
        Tenant tenant = tenantRepository.findByName(req.tenantName())
                .orElseGet(() -> tenantRepository.save(
                        Tenant.builder()
                                .name(req.tenantName())
                                .build()
                ));

        // ---- monta entidade do contrato ----
        PropertyContract contract = PropertyContract.builder()
                .property(property)
                .tenant(tenant)
                .rentValue(req.rentValue())
                .condoValue(req.condoValue())
                .depositValue(req.depositValue())
                .paymentDay(req.paymentDay())
                .monthsInContract(req.monthsInContract())
                .iptuStatus(req.iptuStatus())
                .notes(req.notes())
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        if (req.startDate() != null && !req.startDate().isBlank()) {
            contract.setStartDate(LocalDate.parse(req.startDate(), DATE_FORMAT));
        }

        if (req.endDate() != null && !req.endDate().isBlank()) {
            contract.setEndDate(LocalDate.parse(req.endDate(), DATE_FORMAT));
        }

        PropertyContract saved = contractRepository.save(contract);

        return new ContractHistoryItemDTO(
                saved.getId(),
                saved.getTenant().getName(),
                saved.getStartDate() != null ? saved.getStartDate().format(DATE_FORMAT) : null,
                saved.getEndDate() != null ? saved.getEndDate().format(DATE_FORMAT) : null,
                saved.getRentValue(),
                saved.getStatus().name()
        );
    }

    // 2) ENCERRAR / RESCINDIR CONTRATO
    @PutMapping("/{id}/terminate")
    public ContractHistoryItemDTO terminateContract(
            @PathVariable UUID id,
            @RequestParam String endDate,
            @RequestParam(required = false) String reason
    ) {
        PropertyContract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        contract.setEndDate(LocalDate.parse(endDate, DATE_FORMAT));
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setTerminationReason(reason);

        PropertyContract saved = contractRepository.save(contract);

        return new ContractHistoryItemDTO(
                saved.getId(),
                saved.getTenant().getName(),
                saved.getStartDate() != null ? saved.getStartDate().format(DATE_FORMAT) : null,
                saved.getEndDate() != null ? saved.getEndDate().format(DATE_FORMAT) : null,
                saved.getRentValue(),
                saved.getStatus().name()
        );
    }

    // 3) LISTA GERAL DE CONTRATOS (opcional)
    @GetMapping
    public List<ContractHistoryItemDTO> listAllContracts() {
        return contractRepository.findAll().stream()
                .map(c -> new ContractHistoryItemDTO(
                        c.getId(),
                        c.getTenant().getName(),
                        c.getStartDate() != null ? c.getStartDate().format(DATE_FORMAT) : null,
                        c.getEndDate() != null ? c.getEndDate().format(DATE_FORMAT) : null,
                        c.getRentValue(),
                        c.getStatus().name()
                ))
                .toList();
    }
}
