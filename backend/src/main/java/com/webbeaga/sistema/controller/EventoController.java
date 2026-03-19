package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoResponse>>> listar(
            @RequestParam(required = false) Boolean proximos) {
        List<EventoResponse> lista = Boolean.TRUE.equals(proximos)
            ? eventoService.listarProximos()
            : eventoService.listarTodos();
        return ResponseEntity.ok(ApiResponse.ok(lista));
    }

    @GetMapping("/periodo")
    public ResponseEntity<ApiResponse<List<EventoResponse>>> porPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.ok(eventoService.listarPorPeriodo(inicio, fim)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventoResponse>> criar(
            @Valid @RequestBody EventoRequest req,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok("Evento criado", eventoService.criar(req, u)));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventoResponse>> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody EventoStatusRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(eventoService.atualizarStatus(id, req.getStatus())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        eventoService.excluir(id);
        return ResponseEntity.ok(ApiResponse.ok("Evento removido", null));
    }
}
