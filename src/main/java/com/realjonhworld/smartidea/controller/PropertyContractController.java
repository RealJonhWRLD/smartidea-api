// src/main/java/com/realjonhworld/smartidea/controller/PropertyContractController.java
package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.model.*;
import com.realjonhworld.smartidea.repository.PropertyContractRepository;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/v1/contracts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyContractController {

    private final PropertyContractRepository contractRepository;
    private final PropertyRepository propertyRepository;

    private static final DateTimeFormatter BR_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            if (value.contains("/")) return LocalDate.parse(value, BR_DATE);
            return LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatBr(LocalDate date) {
        if (date == null) return null;
        return date.format(BR_DATE);
    }

    // DTO simples pra criação/edição de contrato vindo do front
    public static class ContractRequest {
        public UUID propertyId;
        public String tenantName;
        public String tenantPhone;
        public String rentValue;
        public String depositValue;
        public String condoValue;
        public String paymentDay;
        public String startDate; // dd/MM/yyyy
        public String endDate;   // opcional
        public Integer monthsLate;
        public String terminationReason;
    }

    // 👉 Criar novo contrato para um imóvel
    @PostMapping
    public ResponseEntity<PropertyContract> createContract(@RequestBody ContractRequest request) {
        if (request.propertyId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "propertyId é obrigatório");
        }

        Property property = propertyRepository.findById(request.propertyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Imóvel não encontrado"));

        // Regra: não pode ter mais de 1 contrato ACTIVE
        contractRepository.findByPropertyIdAndStatus(property.getId(), ContractStatus.ACTIVE)
                .ifPresent(c -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Imóvel já possui contrato ativo. Encerre o contrato atual antes de criar outro."
                    );
                });

        LocalDate start = parseDate(request.startDate);
        LocalDate end = parseDate(request.endDate);

        if (start == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de início do contrato é obrigatória");
        }

        if (end != null && end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data final não pode ser antes da inicial");
        }

        Integer monthsInContract = null;
        if (end != null) {
            long months = ChronoUnit.MONTHS.between(
                    start.withDayOfMonth(1),
                    end.withDayOfMonth(1)
            ) + 1;
            monthsInContract = (int) months;
        }

        PropertyContract contract = PropertyContract.builder()
                .property(property)
                .tenantName(request.tenantName)
                .tenantPhone(request.tenantPhone)
                .rentValue(request.rentValue)
                .depositValue(request.depositValue)
                .condoValue(request.condoValue)
                .paymentDay(request.paymentDay)
                .startDate(start)
                .endDate(end)
                .monthsInContract(monthsInContract)
                .monthsLate(request.monthsLate != null ? request.monthsLate : 0)
                .status(ContractStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        PropertyContract saved = contractRepository.save(contract);

        // opcional: sincronizar "snapshot" no Property (inquilino atual, datas atuais, etc.)
        property.setClientName(request.tenantName);
        property.setClientPhone(request.tenantPhone);
        property.setRentValue(request.rentValue);
        property.setDepositValue(request.depositValue);
        property.setCondoValue(request.condoValue);
        property.setRentDueDate(request.paymentDay);
        property.setContractStartDate(formatBr(start));
        property.setContractDueDate(end != null ? formatBr(end) : null);
        propertyRepository.save(property);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 👉 Listar contratos de um imóvel (histórico)
    @GetMapping("/property/{propertyId}")
    public List<PropertyContract> listByProperty(@PathVariable UUID propertyId) {
        return contractRepository.findByPropertyIdOrderByStartDateDesc(propertyId);
    }

    // 👉 Encerrar contrato (rescisão)
    public static class TerminateRequest {
        public String terminationDate;   // dd/MM/yyyy
        public String terminationReason; // opcional
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<PropertyContract> terminateContract(
            @PathVariable UUID id,
            @RequestBody TerminateRequest request
    ) {
        PropertyContract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado"));

        if (contract.getStatus() == ContractStatus.TERMINATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contrato já está encerrado");
        }

        LocalDate terminationDate = parseDate(request.terminationDate);
        if (terminationDate == null) {
            terminationDate = LocalDate.now();
        }

        if (terminationDate.isBefore(contract.getStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de rescisão não pode ser antes do início");
        }

        long months = ChronoUnit.MONTHS.between(
                contract.getStartDate().withDayOfMonth(1),
                terminationDate.withDayOfMonth(1)
        ) + 1;
        contract.setMonthsInContract((int) months);
        contract.setEndDate(terminationDate);
        contract.setStatus(ContractStatus.TERMINATED);
        contract.setTerminationReason(request.terminationReason);

        PropertyContract saved = contractRepository.save(contract);

        // opcional: limpar snapshot no imóvel
        Property property = contract.getProperty();
        property.setPropertyStatus("Disponível");
        property.setClientName(null);
        property.setClientPhone(null);
        propertyRepository.save(property);

        return ResponseEntity.ok(saved);
    }
}
