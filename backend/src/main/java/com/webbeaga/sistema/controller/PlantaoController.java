package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.service.PlantaoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plantoes")
public class PlantaoController {

    private final PlantaoService plantaoService;

    public PlantaoController(PlantaoService plantaoService) {
        this.plantaoService = plantaoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlantaoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(plantaoService.listarTodos()));
    }

    @GetMapping("/periodo")
    public ResponseEntity<ApiResponse<List<PlantaoResponse>>> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.ok(plantaoService.listarPorPeriodo(inicio, fim)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlantaoResponse>> criar(
            @Valid @RequestBody PlantaoRequest req,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok("Plantão criado", plantaoService.criar(req, u)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        plantaoService.excluir(id);
        return ResponseEntity.ok(ApiResponse.ok("Plantão removido", null));
    }
}
