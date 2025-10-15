package com.example.backend.service;

import com.example.backend.dto.gestion.GestionLiteDto;
import com.example.backend.dto.semestre.SemestreResponseDto;
import com.example.backend.models.Gestion;
import com.example.backend.models.Semestre;
import com.example.backend.repository.SemestreRepository;
import org.springframework.stereotype.Service;

@Service
public class SemestreService {
    private final SemestreRepository semestreRepository;
    public SemestreService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }

    public SemestreResponseDto getSemestre(Long id) {
        Semestre s = semestreRepository.findById(id).orElseThrow();
        Gestion g = s.getGestion();

        GestionLiteDto gLite = new GestionLiteDto(g.getId(), g.getAno());
        return new SemestreResponseDto(s.getId(), s.getNombre(), gLite);
    }
}
