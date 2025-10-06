package com.example.backend.dto;

public record RegisterRequest(
        String nombre,
        String apellido,
        String email,
        String password,
        String telefono,
        String rol
) {}
