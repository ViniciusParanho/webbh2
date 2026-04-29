package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Erro;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.ErroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ErroService {

    private final ErroRepository erroRepo;

    public ErroService(ErroRepository erroRepo) {
        this.erroRepo = erroRepo;
    }

    @Transactional(readOnly = true)
    public List<ErroResponse> listarTodos() {
        return erroRepo.findAllByOrderByDataCriacaoDesc()
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ErroResponse buscarPorId(Long id) {
        Erro e = erroRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Erro não encontrado"));
        return toResponse(e);
    }

    @Transactional
    public ErroResponse criar(ErroRequest req, Usuario criador) {
        Erro e = new Erro();
        e.setTitulo(req.getTitulo());
        e.setDescricao(req.getDescricao());
        e.setCriador(criador);
        if (req.getTipoErro() != null) {
            try { e.setTipoErro(Erro.TipoErro.valueOf(req.getTipoErro())); }
            catch (IllegalArgumentException ignored) {}
        }
        return toResponse(erroRepo.save(e));
    }

    @Transactional
    public ErroResponse salvarSolucao(Long id, SolucaoRequest req) {
        Erro e = erroRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Erro não encontrado"));
        e.setSolucao(req.getSolucao());
        e.setScript(req.getScript());
        if (req.getSolucao() != null && !req.getSolucao().isBlank()) {
            e.setStatus(Erro.StatusErro.RESOLVIDO);
        }
        return toResponse(erroRepo.save(e));
    }

    @Transactional
    public void excluir(Long id) {
        Erro e = erroRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Erro não encontrado"));
        erroRepo.delete(e);
    }

    private ErroResponse toResponse(Erro e) {
        ErroResponse r = new ErroResponse();
        r.setId(e.getId());
        r.setTitulo(e.getTitulo());
        r.setDescricao(e.getDescricao());
        r.setSolucao(e.getSolucao());
        r.setScript(e.getScript());
        r.setStatus(e.getStatus().name());
        r.setTipoErro(e.getTipoErro() != null ? e.getTipoErro().name() : "OUTROS");
        r.setDataCriacao(e.getDataCriacao());
        if (e.getCriador() != null) {
            r.setCriadorId(e.getCriador().getId());
            r.setCriadorNome(e.getCriador().getNomeCompleto());
        }
        return r;
    }
}
