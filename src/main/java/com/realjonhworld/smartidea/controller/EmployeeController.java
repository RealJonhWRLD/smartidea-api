package com.realjonhworld.smartidea.controller;

import com.realjonhworld.smartidea.model.Employee;
import com.realjonhworld.smartidea.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Garante que o React pode acessar
public class EmployeeController {

    private final EmployeeRepository repository;

    // Listar todos
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Cadastrar novo
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(repository.save(employee));
    }

    // Atualizar funcionário existente
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable UUID id, @RequestBody Employee updatedData) {
        return repository.findById(id)
                .map(employee -> {
                    // Atualiza os campos (você pode escolher quais permite editar)
                    employee.setName(updatedData.getName());
                    employee.setRole(updatedData.getRole());
                    employee.setPhone(updatedData.getPhone());
                    employee.setEmail(updatedData.getEmail());
                    employee.setCpf(updatedData.getCpf());
                    employee.setLocation(updatedData.getLocation());
                    employee.setSalary(updatedData.getSalary());
                    employee.setBirthDate(updatedData.getBirthDate());
                    employee.setPixKey(updatedData.getPixKey());
                    employee.setSocialLinks(updatedData.getSocialLinks());

                    // Se mandou senha nova, atualiza. Se não, mantém a antiga.
                    if(updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
                        employee.setPassword(updatedData.getPassword());
                    }

                    return ResponseEntity.ok(repository.save(employee));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}