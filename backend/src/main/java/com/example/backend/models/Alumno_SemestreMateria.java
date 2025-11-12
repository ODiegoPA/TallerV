package com.example.backend.models;

import jakarta.persistence.*;

@Entity
public class Alumno_SemestreMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private User alumno;

    @ManyToOne
    @JoinColumn(name = "semestre_materia_id")
    private SemestreMateria semestreMateria;

    private Integer faltas;
    private Boolean estaAprovado;
}
