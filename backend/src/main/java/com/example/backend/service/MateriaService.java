package com.example.backend.service;

import com.example.backend.dto.materia.MateriaRequestDto;
import com.example.backend.dto.materia.MateriaResponseDto;
import com.example.backend.dto.semestre.SemestreLiteDto;
import com.example.backend.dto.user.UserLiteDto;
import com.example.backend.models.Materia;
import com.example.backend.models.Semestre;
import com.example.backend.models.User;
import com.example.backend.repository.MateriaRepository;
import com.example.backend.repository.SemestreRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MateriaService {
    private final MateriaRepository materiaRepository;
    private final SemestreService semestreService;
    private final UserService userService;
    private final SemestreRepository semestreRepository;
    private final UserRepository userRepository;

    public MateriaService(MateriaRepository materiaRepository, SemestreService semestreService, UserService userService, SemestreRepository semestreRepository, UserRepository userRepository) {
        this.materiaRepository = materiaRepository;
        this.semestreService = semestreService;
        this.userService = userService;
        this.semestreRepository = semestreRepository;
        this.userRepository = userRepository;
    }

    public MateriaResponseDto getMateria(Long id){
        Materia m = materiaRepository.findById(id).orElseThrow();
        Semestre s = m.getSemestre();
        User d = m.getDocente();

        SemestreLiteDto sLite = new SemestreLiteDto(s.getId(), s.getNombre(), s.getFechaInicio(), s.getFechaFin());
        UserLiteDto dLite = new UserLiteDto(d.getId(), d.getNombre(), d.getApellido());

        return new MateriaResponseDto(m.getId(), m.getNombre(), m.getCupos(), m.getEstado(), sLite, dLite);
    }
    public List<MateriaResponseDto> getAll() {
        List<Materia> materias = materiaRepository.findAll();
        return materias.stream().map(this::toDto).toList();
    }

    public MateriaResponseDto create(MateriaRequestDto dto){
        Semestre s = semestreRepository.findById(dto.semestreId()).orElseThrow();
        User d = userRepository.findById(dto.docenteId()).orElseThrow();
        Materia m = new Materia();
        m.setNombre(dto.nombre());
        m.setCupos(dto.cupos());
        m.setEstado(dto.estado());
        m.setSemestre(s);
        m.setDocente(d);
        materiaRepository.save(m);
        return toDto(materiaRepository.findById(m.getId()).orElse(m));
    }

    public MateriaResponseDto update(Long id, MateriaRequestDto dto) {
        Materia m = materiaRepository.findById(id).orElseThrow();
        m.setNombre(dto.nombre());
        m.setCupos(dto.cupos());
        Semestre s = semestreRepository.findById(dto.semestreId()).orElseThrow();
        m.setSemestre(s);
        User d = userRepository.findById(dto.docenteId()).orElseThrow();
        m.setDocente(d);
        materiaRepository.save(m);
        return toDto(materiaRepository.findById(m.getId()).orElse(m));
    }

    public void delete(Long id){
        Materia m = materiaRepository.findById(id).orElseThrow();
        materiaRepository.delete(m);
    }

    private MateriaResponseDto toDto(Materia m) {
        Semestre s = m.getSemestre();
        User d = m.getDocente();

        SemestreLiteDto sLite = new SemestreLiteDto(s.getId(), s.getNombre(), s.getFechaInicio(), s.getFechaFin());
        UserLiteDto dLite = new UserLiteDto(d.getId(), d.getNombre(), d.getApellido());

        return new MateriaResponseDto(m.getId(), m.getNombre(), m.getCupos(), m.getEstado(), sLite, dLite);
    }
}
