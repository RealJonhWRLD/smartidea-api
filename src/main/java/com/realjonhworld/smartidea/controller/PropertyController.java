package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.model.Property;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyController {

    private final PropertyRepository repository;

    // Formato dd/MM/yyyy (igual do front)
    private static final DateTimeFormatter BR_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Tenta converter uma String em LocalDate (aceita dd/MM/yyyy ou yyyy-MM-dd)
    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            if (value.contains("/")) {
                // dd/MM/yyyy
                return LocalDate.parse(value, BR_DATE);
            }
            // yyyy-MM-dd
            return LocalDate.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calcula a quantidade de meses entre início e fim de contrato e grava em contractMonths.
     * Regra simples: diferença em meses entre os meses das datas (inclusivo).
     * Ex: 01/01/2025 a 01/06/2025 => 6 meses.
     */
    private void calcularMesesContrato(Property property) {
        LocalDate inicio = parseDate(property.getContractStartDate());
        LocalDate fim = parseDate(property.getContractDueDate());

        if (inicio == null || fim == null) {
            property.setContractMonths(null);
            return;
        }

        if (fim.isBefore(inicio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data final do contrato não pode ser antes da data inicial."
            );
        }

        long meses = ChronoUnit.MONTHS.between(
                inicio.withDayOfMonth(1),
                fim.withDayOfMonth(1)
        ) + 1; // +1 para incluir o mês do início

        property.setContractMonths(String.valueOf(meses));
    }

    /**
     * Verifica se já existe contrato ativo para o imóvel e se estão tentando trocar o inquilino.
     * Se sim, bloqueia.
     */
    private void validarTrocaInquilinoContratoAtivo(Property existing, Property updatedData) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioExist = parseDate(existing.getContractStartDate());
        LocalDate fimExist = parseDate(existing.getContractDueDate());

        boolean contratoAtivo =
                inicioExist != null &&
                        fimExist != null &&
                        !hoje.isBefore(inicioExist) && // hoje >= início
                        !hoje.isAfter(fimExist);       // hoje <= fim

        String inquilinoAtual = existing.getClientName();
        String inquilinoNovo = updatedData.getClientName();

        boolean trocandoInquilino =
                inquilinoNovo != null &&
                        !inquilinoNovo.isBlank() &&
                        inquilinoAtual != null &&
                        !inquilinoAtual.isBlank() &&
                        !inquilinoAtual.equals(inquilinoNovo);

        if (contratoAtivo && trocandoInquilino) {
            String msg = "Imóvel já possui contrato ativo até "
                    + fimExist.format(BR_DATE)
                    + ". Não é permitido cadastrar um novo inquilino enquanto o contrato estiver ativo.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
        }
    }

    // Buscar todos
    @GetMapping
    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    // Salvar novo imóvel
    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        // calcula meses de contrato se vierem as duas datas
        calcularMesesContrato(property);
        Property saved = repository.save(property);
        return ResponseEntity.ok(saved);
    }

    // Atualizar imóvel
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(
            @PathVariable UUID id,
            @RequestBody Property updatedData
    ) {
        return repository.findById(id)
                .map(existing -> {

                    // 1) Regra: não pode trocar de inquilino se já tem contrato ativo
                    validarTrocaInquilinoContratoAtivo(existing, updatedData);

                    // 2) Atualiza os campos
                    existing.setName(updatedData.getName());
                    existing.setPropertyType(updatedData.getPropertyType());
                    existing.setDescription(updatedData.getDescription());

                    existing.setClientName(updatedData.getClientName());
                    existing.setClientPhone(updatedData.getClientPhone());

                    existing.setMatricula(updatedData.getMatricula());
                    existing.setCagece(updatedData.getCagece());
                    existing.setEnel(updatedData.getEnel());
                    existing.setLastRenovation(updatedData.getLastRenovation());
                    existing.setPropertyStatus(updatedData.getPropertyStatus());

                    existing.setRentValue(updatedData.getRentValue());
                    existing.setCondoValue(updatedData.getCondoValue());
                    existing.setDepositValue(updatedData.getDepositValue());
                    existing.setIptuStatus(updatedData.getIptuStatus());

                    existing.setRentDueDate(updatedData.getRentDueDate());
                    existing.setContractStartDate(updatedData.getContractStartDate());
                    existing.setContractDueDate(updatedData.getContractDueDate());
                    existing.setRentPaymentStatus(updatedData.getRentPaymentStatus());
                    existing.setNotes(updatedData.getNotes());

                    existing.setLat(updatedData.getLat());
                    existing.setLng(updatedData.getLng());

                    // 3) Recalcula meses de contrato baseado nas novas datas
                    calcularMesesContrato(existing);

                    Property saved = repository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar imóvel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
