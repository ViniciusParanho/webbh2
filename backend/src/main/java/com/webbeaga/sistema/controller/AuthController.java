package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService  = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest req) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            Usuario u = (Usuario) auth.getPrincipal();
            String token = jwtService.generateToken(u);

            LoginResponse resp = new LoginResponse();
            resp.setToken(token);
            resp.setId(u.getId());
            resp.setUsername(u.getUsername());
            resp.setNomeCompleto(u.getNomeCompleto());
            resp.setCargo(u.getCargo());
            resp.setRole(u.getRole().name());

            return ResponseEntity.ok(ApiResponse.ok(resp));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Usuario ou senha incorretos"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<LoginResponse>> me(Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        LoginResponse resp = new LoginResponse();
        resp.setId(u.getId());
        resp.setUsername(u.getUsername());
        resp.setNomeCompleto(u.getNomeCompleto());
        resp.setCargo(u.getCargo());
        resp.setRole(u.getRole().name());
        return ResponseEntity.ok(ApiResponse.ok(resp));
    }
}
