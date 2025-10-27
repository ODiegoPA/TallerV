package com.example.backend.dto.materia;

public record MateriaRequestDto (String nombre, Integer cupos, String estado, Long semestreId, Long docenteId) {
}
