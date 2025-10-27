package com.example.backend.repository;

import com.example.backend.models.Materia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    @EntityGraph(attributePaths = {"semestre", "docente"})
    Optional<Materia> findById(@Param("id") Long id);
}
