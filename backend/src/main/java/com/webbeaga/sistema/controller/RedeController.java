package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.service.RedePostoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/redes")
public class RedeController {

    private final RedePostoService redePostoService;

    public RedeController(RedePostoService redePostoService) {
        this.redePostoService = redePostoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RedeResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(redePostoService.listarRedes()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RedeResponse>> criar(@Valid @RequestBody RedeRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(redePostoService.criarRede(req)));
    }

    @PostMapping("/postos")
    public ResponseEntity<ApiResponse<PostoResponse>> criarPosto(@Valid @RequestBody PostoRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(redePostoService.criarPosto(req)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirRede(@PathVariable Long id) {
        redePostoService.excluirRede(id);
        return ResponseEntity.ok(ApiResponse.ok("Rede removida", null));
    }

    @DeleteMapping("/postos/{id}")
    public ResponseEntity<ApiResponse<Void>> excluirPosto(@PathVariable Long id) {
        redePostoService.excluirPosto(id);
        return ResponseEntity.ok(ApiResponse.ok("Posto removido", null));
    }

    @PatchMapping("/postos/{postoId}/online")
    public ResponseEntity<ApiResponse<PostoResponse>> atualizarOnline(
            @PathVariable Long postoId,
            @RequestParam boolean online) {
        return ResponseEntity.ok(ApiResponse.ok(redePostoService.atualizarOnline(postoId, online)));
    }
}
