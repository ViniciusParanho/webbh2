package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.listarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> criar(@Valid @RequestBody UsuarioRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Usuario criado com sucesso", usuarioService.criar(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> atualizar(
            @PathVariable Long id,
            @RequestBody UsuarioRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(usuarioService.atualizar(id, req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> desativar(@PathVariable Long id) {
        usuarioService.desativar(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario desativado", null));
    }
}
