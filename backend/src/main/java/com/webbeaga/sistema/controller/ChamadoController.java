package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.service.ChamadoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService cs) {
        this.chamadoService = cs;
    }

    @GetMapping("/em-andamento-ids")
    public ResponseEntity<DTOs.ApiResponse<List<Long>>> emAndamentoIds() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.listarUsuariosEmAtendimento()));
    }

    @GetMapping
    public ResponseEntity<DTOs.ApiResponse<List<DTOs.ChamadoResponse>>> listar(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<DTOs.ChamadoResponse> lista = isAdmin
                ? chamadoService.listarTodos()
                : chamadoService.listarMeus(auth.getName());
        return ResponseEntity.ok(DTOs.ApiResponse.ok(lista));
    }

    @PostMapping
    public ResponseEntity<DTOs.ApiResponse<DTOs.ChamadoResponse>> criar(
            @RequestBody DTOs.ChamadoRequest req, Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.criar(req, auth.getName())));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<DTOs.ApiResponse<DTOs.ChamadoResponse>> finalizar(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.finalizar(id, auth.getName())));
    }

    @GetMapping("/ranking-diario")
    public ResponseEntity<DTOs.ApiResponse<List<DTOs.RankingItem>>> rankingDiario() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.rankingDiario()));
    }

    @GetMapping("/produtividade")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DTOs.ApiResponse<List<DTOs.ChamadoResponse>>> produtividade(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.listarProdutividade(usuarioId, inicio, fim)));
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<DTOs.ApiResponse<DTOs.ChamadoResponse>> reabrir(
            @PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.reabrir(id, auth.getName())));
    }

    @PatchMapping("/{id}/ticket")
    public ResponseEntity<DTOs.ApiResponse<DTOs.ChamadoResponse>> vincularTicket(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body,
            Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.vincularTicket(id, body.get("ticket"), auth.getName())));
    }

    @PatchMapping("/{id}/transferir")
    public ResponseEntity<DTOs.ApiResponse<DTOs.ChamadoResponse>> transferir(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Long> body,
            Authentication auth) {
        Long novoUsuarioId = body.get("usuarioId");
        return ResponseEntity.ok(DTOs.ApiResponse.ok(chamadoService.transferir(id, novoUsuarioId, auth.getName())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DTOs.ApiResponse<Void>> excluir(@PathVariable Long id) {
        chamadoService.excluir(id);
        return ResponseEntity.ok(DTOs.ApiResponse.ok("Chamado removido", null));
    }
}
