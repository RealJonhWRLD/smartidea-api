package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.model.Property;
import com.realjonhworld.smartidea.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyController {

    private final PropertyRepository repository;

    // Buscar todos
    @GetMapping
    public List<Property> getAllProperties() {
        return repository.findAll();
    }

    // Salvar novo
    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        return ResponseEntity.ok(repository.save(property));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable UUID id, @RequestBody Property updatedData) {
        return repository.findById(id)
                .map(property -> {
                    // Atualiza TUDO
                    property.setName(updatedData.getName());
                    property.setPropertyType(updatedData.getPropertyType());
                    property.setDescription(updatedData.getDescription());

                    property.setClientName(updatedData.getClientName());
                    property.setClientPhone(updatedData.getClientPhone()); // Novo

                    property.setMatricula(updatedData.getMatricula());
                    property.setCagece(updatedData.getCagece()); // Novo
                    property.setEnel(updatedData.getEnel()); // Novo
                    property.setLastRenovation(updatedData.getLastRenovation()); // Novo
                    property.setPropertyStatus(updatedData.getPropertyStatus()); // Novo

                    property.setRentValue(updatedData.getRentValue());
                    property.setCondoValue(updatedData.getCondoValue()); // Novo
                    property.setDepositValue(updatedData.getDepositValue()); // Novo
                    property.setIptuStatus(updatedData.getIptuStatus());

                    property.setRentDueDate(updatedData.getRentDueDate());
                    property.setContractDueDate(updatedData.getContractDueDate());
                    property.setContractMonths(updatedData.getContractMonths()); // Novo
                    property.setRentPaymentStatus(updatedData.getRentPaymentStatus());

                    property.setNotes(updatedData.getNotes()); // Novo

                    property.setLat(updatedData.getLat());
                    property.setLng(updatedData.getLng());

                    return ResponseEntity.ok(repository.save(property));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Deletar (Para o botão de Lixeira funcionar)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}