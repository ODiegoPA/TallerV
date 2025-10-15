package com.example.backend.service;

import com.example.backend.dto.gestion.GestionRequestDto;
import com.example.backend.dto.gestion.GestionResponseDto;
import com.example.backend.dto.semestre.SemestreLiteDto;
import com.example.backend.models.Gestion;
import com.example.backend.models.Semestre;
import com.example.backend.repository.GestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GestionService {
    private final GestionRepository gestionRepository;

    public GestionService(GestionRepository gestionRepository) {
        this.gestionRepository = gestionRepository;
    }

    public GestionResponseDto getGestion(Long id){
        Gestion g = gestionRepository.findById(id).orElseThrow();
        List<SemestreLiteDto> semestres = g.getSemestres().stream().map
                (s -> new SemestreLiteDto(s.getId(), s.getNombre(), s.getFechaInicio(), s.getFechaFin())).toList();
        return new GestionResponseDto(g.getId(), g.getAno(), semestres);
    }

    public List<GestionResponseDto> getAll() {
        List<Gestion> gestiones = gestionRepository.findAll();
        return gestiones.stream().map(this::toDto).toList();
    }

    public GestionResponseDto create(GestionRequestDto dto) {
        Gestion g = new Gestion();
        g.setAno(dto.ano());
        gestionRepository.save(g);
        return toDto(gestionRepository.findById(g.getId()).orElse(g));
    }

    public GestionResponseDto update(Long id, GestionRequestDto dto) {
        Gestion g = gestionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestión " + id + " no encontrada")
        );
        g.setAno(dto.ano());
        gestionRepository.save(g);
        return toDto(gestionRepository.findById(g.getId()).orElse(g));
    }

    public void delete(Long id) {
        Gestion g = gestionRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestión " + id + " no encontrada")
        );

        if (g.getSemestres() != null && !g.getSemestres().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar la gestión porque tiene semestres asociados.");
        }

        gestionRepository.delete(g);
    }
    private GestionResponseDto toDto(Gestion g) {
        List<SemestreLiteDto> semestres = g.getSemestres() == null ? List.of()
                : g.getSemestres().stream().map(this::toLite).toList();
        return new GestionResponseDto(g.getId(), g.getAno(), semestres);
    }

    private SemestreLiteDto toLite(Semestre s) {
        return new SemestreLiteDto(s.getId(), s.getNombre(), s.getFechaInicio(), s.getFechaFin());
    }
}
