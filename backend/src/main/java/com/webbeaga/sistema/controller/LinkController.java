package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.service.LinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService ls) {
        this.linkService = ls;
    }

    @GetMapping
    public ResponseEntity<DTOs.ApiResponse<List<DTOs.LinkResponse>>> listar() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(linkService.listarTodos()));
    }

    @PostMapping
    public ResponseEntity<DTOs.ApiResponse<DTOs.LinkResponse>> criar(
            @RequestBody DTOs.LinkRequest req, Authentication auth) {
        return ResponseEntity.ok(DTOs.ApiResponse.ok(linkService.criar(req, auth.getName())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DTOs.ApiResponse<Void>> excluir(@PathVariable Long id) {
        linkService.excluir(id);
        return ResponseEntity.ok(DTOs.ApiResponse.ok("Link removido", null));
    }
}
