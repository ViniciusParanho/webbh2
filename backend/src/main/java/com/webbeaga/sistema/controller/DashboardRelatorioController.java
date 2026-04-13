package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.service.ChamadoService;
import com.webbeaga.sistema.service.DashboardService;
import com.webbeaga.sistema.service.RelatorioService;
import com.webbeaga.sistema.service.RelatorioService.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DashboardRelatorioController {

    private final DashboardService dashboardService;
    private final RelatorioService relatorioService;
    private final ChamadoService   chamadoService;

    public DashboardRelatorioController(DashboardService ds, RelatorioService rs, ChamadoService cs) {
        this.dashboardService = ds;
        this.relatorioService = rs;
        this.chamadoService   = cs;
    }

    @GetMapping("/api/dashboard")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getDashboard()));
    }

    @GetMapping("/api/relatorios/equipe")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ResumoEquipeResponse>> getResumoEquipe(
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes) {
        LocalDateTime agora = LocalDateTime.now();
        int a = ano > 0 ? ano : agora.getYear();
        int m = mes > 0 ? mes : agora.getMonthValue();
        return ResponseEntity.ok(ApiResponse.ok(relatorioService.getResumoEquipe(a, m)));
    }

    @GetMapping("/api/relatorios/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RelatorioMesResponse>>> getTodos(
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes) {
        LocalDateTime agora = LocalDateTime.now();
        int a = ano > 0 ? ano : agora.getYear();
        int m = mes > 0 ? mes : agora.getMonthValue();
        return ResponseEntity.ok(ApiResponse.ok(relatorioService.getRelatorioEquipe(a, m)));
    }

    @GetMapping("/api/relatorios/usuario/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RelatorioMesResponse>> getUsuario(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes) {
        LocalDateTime agora = LocalDateTime.now();
        int a = ano > 0 ? ano : agora.getYear();
        int m = mes > 0 ? mes : agora.getMonthValue();
        return ResponseEntity.ok(ApiResponse.ok(relatorioService.getRelatorioUsuario(id, a, m)));
    }

    @GetMapping("/api/relatorios/meu")
    public ResponseEntity<ApiResponse<RelatorioMesResponse>> getMeuRelatorio(
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        LocalDateTime agora = LocalDateTime.now();
        int a = ano > 0 ? ano : agora.getYear();
        int m = mes > 0 ? mes : agora.getMonthValue();
        return ResponseEntity.ok(ApiResponse.ok(relatorioService.getRelatorioUsuario(u.getId(), a, m)));
    }

    @GetMapping("/api/relatorios/atendimentos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DTOs.RelatorioAtendimentosResponse>> getAtendimentos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(ApiResponse.ok(chamadoService.getRelatorioAtendimentos(inicio, fim)));
    }
}
