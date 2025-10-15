package com.example.backend.repository;

import com.example.backend.models.Gestion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GestionRepository extends JpaRepository<Gestion, Long> {
    @EntityGraph(attributePaths = "semestres")
    Optional<Gestion> findById(Long id);
}
