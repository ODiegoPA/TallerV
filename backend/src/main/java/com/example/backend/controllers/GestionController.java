package com.example.backend.controllers;

import com.example.backend.dto.gestion.GestionRequestDto;
import com.example.backend.dto.gestion.GestionResponseDto;
import com.example.backend.service.GestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gestion")
public class GestionController {

    private final GestionService gestionService;
    public GestionController(GestionService gestionService) {
        this.gestionService = gestionService;
    }

    @GetMapping("/")
    public List<GestionResponseDto> getAll() {
        return gestionService.getAll();
    }

    @GetMapping("/{id}")
    public GestionResponseDto get(@PathVariable Long id) {
        return gestionService.getGestion(id);
    }
    @PostMapping("/")
    public GestionResponseDto create(@RequestBody GestionRequestDto dto) {
        return gestionService.create(dto);
    }
    @PutMapping("/{id}")
    public GestionResponseDto update(@PathVariable Long id, @RequestBody GestionRequestDto dto) {
        return gestionService.update(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gestionService.delete(id);
    }

    //PH: Borrar despues
    @GetMapping("/me")
    public Map<String, Object> me(org.springframework.security.core.Authentication auth) {
        if (auth == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "No autenticado");
        }
        var authorities = auth.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList();
        return java.util.Map.of("name", auth.getName(), "authorities", authorities);
    }
}
