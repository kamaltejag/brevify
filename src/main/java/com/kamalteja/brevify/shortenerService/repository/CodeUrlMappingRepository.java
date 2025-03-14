package com.kamalteja.brevify.shortenerService.repository;

import com.kamalteja.brevify.shortenerService.model.CodeUrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodeUrlMappingRepository extends JpaRepository<CodeUrlMapping, UUID> {
    Optional<CodeUrlMapping> findByShortCode(String shortCode);
}
