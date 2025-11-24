package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    // Aqui podemos criar buscas no futuro, ex: findByRole(String role);
}