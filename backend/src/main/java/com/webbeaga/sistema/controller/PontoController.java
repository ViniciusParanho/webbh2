package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.RegistroPontoRepository;
import com.webbeaga.sistema.service.PontoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ponto")
public class PontoController {

    private final PontoService pontoService;
    private final RegistroPontoRepository pontoRepo;

    public PontoController(PontoService pontoService, RegistroPontoRepository pontoRepo) {
        this.pontoService = pontoService;
        this.pontoRepo = pontoRepo;
    }

    @PostMapping("/bater")
    public ResponseEntity<ApiResponse<PontoResponse>> baterPonto(
            @Valid @RequestBody BaterPontoRequest req,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok(pontoService.baterPonto(u, req.getTipo(), req.getLatitude(), req.getLongitude())));
    }

    @GetMapping("/hoje")
    public ResponseEntity<ApiResponse<PontoResponse>> getHoje(Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        PontoResponse p = pontoService.getPontoHoje(u.getId());
        if (p == null) {
            PontoResponse vazio = new PontoResponse();
            vazio.setData(LocalDate.now());
            vazio.setStatus("PENDENTE");
            vazio.setUsuarioId(u.getId());
            vazio.setUsuarioNome(u.getNomeCompleto());
            return ResponseEntity.ok(ApiResponse.ok(vazio));
        }
        return ResponseEntity.ok(ApiResponse.ok(p));
    }

    @GetMapping("/historico")
    public ResponseEntity<ApiResponse<List<PontoResponse>>> getHistorico(
            @RequestParam(defaultValue = "0") int ano,
            @RequestParam(defaultValue = "0") int mes,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        LocalDateTime agora = LocalDateTime.now();
        int a = ano > 0 ? ano : agora.getYear();
        int m = mes > 0 ? mes : agora.getMonthValue();
        return ResponseEntity.ok(ApiResponse.ok(pontoService.getHistorico(u.getId(), a, m)));
    }

    @GetMapping("/localizacoes-hoje")
    public ResponseEntity<ApiResponse<List<PontoResponse>>> getLocalizacoesHoje() {
        List<PontoResponse> result = pontoRepo.findByData(LocalDate.now())
            .stream()
            .filter(p -> p.getHoraEntrada() != null)
            .map(PontoResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/periodo")
    public ResponseEntity<ApiResponse<List<PontoResponse>>> getPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok(pontoService.getPorPeriodo(u.getId(), inicio, fim)));
    }
}
