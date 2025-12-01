package com.realjonhworld.smartidea.repository;

import com.realjonhworld.smartidea.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {
}
