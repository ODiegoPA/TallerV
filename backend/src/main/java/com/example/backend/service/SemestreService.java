package com.example.backend.service;

import com.example.backend.dto.gestion.GestionLiteDto;
import com.example.backend.dto.semestre.SemestreRequestDto;
import com.example.backend.dto.semestre.SemestreResponseDto;
import com.example.backend.models.Gestion;
import com.example.backend.models.Semestre;
import com.example.backend.repository.GestionRepository;
import com.example.backend.repository.SemestreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SemestreService {
    private final SemestreRepository semestreRepository;
    private final GestionRepository gestionRepository;

    public SemestreService(SemestreRepository semestreRepository, GestionRepository gestionRepository) {
        this.semestreRepository = semestreRepository;
        this.gestionRepository = gestionRepository;
    }

    public SemestreResponseDto getSemestre(Long id) {
        Semestre s = semestreRepository.findById(id).orElseThrow();
        Gestion g = s.getGestion();

        GestionLiteDto gLite = new GestionLiteDto(g.getId(), g.getAno());
        return new SemestreResponseDto(s.getId(), s.getNombre(), gLite);
    }
    public List<SemestreResponseDto> getAll() {
        List<Semestre> semestres = semestreRepository.findAll();
        return semestres.stream().map(this::toDto).toList();
    }
    public SemestreResponseDto create(SemestreRequestDto dto) {
        Gestion g = gestionRepository.findById(dto.gestionId()).orElseThrow();
        Semestre s = new Semestre();
        s.setNombre(dto.nombre());
        s.setGestion(g);
        semestreRepository.save(s);
        return toDto(semestreRepository.findById(s.getId()).orElse(s));
    }
    public SemestreResponseDto update(Long id, SemestreRequestDto dto) {
        Semestre s = semestreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre " + id + " no encontrado"));

        s.setNombre(dto.nombre());
        Gestion g = gestionRepository.findById(dto.gestionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestión " + dto.gestionId() + " no encontrada"));
        s.setGestion(g);
        semestreRepository.save(s);
        return toDto(semestreRepository.findById(s.getId()).orElse(s));
    }
    public void delete(Long id) {
        Semestre s = semestreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semestre " + id + " no encontrado"));
        semestreRepository.delete(s);
    }
    private SemestreResponseDto toDto(Semestre s) {
        Gestion g = s.getGestion();
        GestionLiteDto gLite = new GestionLiteDto(g.getId(), g.getAno()); // usa getAnio() si así se llama
        return new SemestreResponseDto(s.getId(), s.getNombre(), gLite);
    }

}
