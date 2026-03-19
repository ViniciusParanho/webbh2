package com.webbeaga.sistema.controller;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.ErroAnexo;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.service.ErroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/erros")
public class ErroController {

    private final ErroService erroService;

    public ErroController(ErroService erroService) {
        this.erroService = erroService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ErroResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(erroService.listarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ErroResponse>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(erroService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ErroResponse>> criar(
            @Valid @RequestBody ErroRequest req,
            Authentication auth) {
        Usuario u = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok("Bug reportado", erroService.criar(req, u)));
    }

    @PatchMapping("/{id}/solucao")
    public ResponseEntity<ApiResponse<ErroResponse>> solucao(
            @PathVariable Long id,
            @RequestBody SolucaoRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Solução salva", erroService.salvarSolucao(id, req)));
    }

    @PostMapping("/{id}/anexos")
    public ResponseEntity<ApiResponse<AnexoResponse>> uploadAnexo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipo") ErroAnexo.TipoAnexo tipo) {
        return ResponseEntity.ok(ApiResponse.ok(erroService.salvarAnexo(id, file, tipo)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long id) {
        erroService.excluir(id);
        return ResponseEntity.ok(ApiResponse.ok("Removido", null));
    }
}
