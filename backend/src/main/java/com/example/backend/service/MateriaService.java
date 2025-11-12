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
    private final SemestreRepository semestreRepository;
    private final UserRepository userRepository;

    public MateriaService(MateriaRepository materiaRepository, SemestreService semestreService, UserService userService, SemestreRepository semestreRepository, UserRepository userRepository) {
        this.materiaRepository = materiaRepository;
        this.semestreRepository = semestreRepository;
        this.userRepository = userRepository;
    }

    public MateriaResponseDto getMateria(Long id){
        Materia m = materiaRepository.findById(id).orElseThrow();
        return new MateriaResponseDto(m.getId(), m.getNombre());
    }
    public List<MateriaResponseDto> getAll() {
        List<Materia> materias = materiaRepository.findAll();
        return materias.stream().map(this::toDto).toList();
    }

    public MateriaResponseDto create(MateriaRequestDto dto){
        Materia m = new Materia();
        m.setNombre(dto.nombre());
        materiaRepository.save(m);
        return toDto(materiaRepository.findById(m.getId()).orElse(m));
    }

    public MateriaResponseDto update(Long id, MateriaRequestDto dto) {
        Materia m = materiaRepository.findById(id).orElseThrow();
        m.setNombre(dto.nombre());
        materiaRepository.save(m);
        return toDto(materiaRepository.findById(m.getId()).orElse(m));
    }

    public void delete(Long id){
        Materia m = materiaRepository.findById(id).orElseThrow();
        materiaRepository.delete(m);
    }

    private MateriaResponseDto toDto(Materia m) {
        return new MateriaResponseDto(m.getId(), m.getNombre());
    }
}
