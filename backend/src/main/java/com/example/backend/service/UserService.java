package com.example.backend.service;

import com.example.backend.dto.AuthResponse;
import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.RegisterRequest;
import com.example.backend.models.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        User u = new User();
        u.setNombre(req.nombre());
        u.setApellido(req.apellido());
        u.setEmail(req.email());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setTelefono(req.telefono());
        u.setRol("Estudiante"); // ← rol por defecto
        // u.setCodigo(...); // si querés generar un código, hacelo acá
        userRepository.save(u);

        String token = jwtService.generateToken(UserPrincipal.of(u));
        return new AuthResponse(token, u.getEmail(), u.getNombre(), u.getRol());
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(UserPrincipal.of(u));
        return new AuthResponse(token, u.getEmail(), u.getNombre(), u.getRol());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UserPrincipal.of(u);
    }

    // Adaptador a UserDetails
    private record UserPrincipal(String username, String password, String role) implements UserDetails {
        static UserPrincipal of(User u) {
            String role = (u.getRol() == null || u.getRol().isBlank()) ? "Estudiante" : u.getRol();
            return new UserPrincipal(u.getEmail(), u.getPassword(), role);
        }
        @Override public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        }
        @Override public String getPassword() { return password; }
        @Override public String getUsername() { return username; }
        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }
    }
}
