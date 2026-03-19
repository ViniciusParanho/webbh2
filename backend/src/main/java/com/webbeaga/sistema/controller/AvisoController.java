package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.service.AvisoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/avisos")
public class AvisoController {

    private final AvisoService avisoService;

    public AvisoController(AvisoService as) {
        this.avisoService = as;
    }

    @GetMapping
    public ResponseEntity<DTOs.ApiResponse<List<DTOs.AvisoResponse>>> listar() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(avisoService.listarTodos()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOs.ApiResponse<DTOs.AvisoResponse>> criar(
            @RequestBody DTOs.AvisoRequest req, Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(avisoService.criar(req, auth.getName())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOs.ApiResponse<Void>> excluir(@PathVariable Long id) {
        avisoService.excluir(id);
        return ResponseEntity.ok(DTOs.ApiResponse.ok("Aviso removido", null));
    }
}
