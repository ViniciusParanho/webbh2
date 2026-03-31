package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Posto;
import com.webbeaga.sistema.entity.Rede;
import com.webbeaga.sistema.repository.ChamadoRepository;
import com.webbeaga.sistema.repository.PostoRepository;
import com.webbeaga.sistema.repository.RedeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedePostoService {

    private final RedeRepository redeRepo;
    private final PostoRepository postoRepo;
    private final ChamadoRepository chamadoRepo;

    public RedePostoService(RedeRepository redeRepo, PostoRepository postoRepo, ChamadoRepository chamadoRepo) {
        this.redeRepo     = redeRepo;
        this.postoRepo    = postoRepo;
        this.chamadoRepo  = chamadoRepo;
    }

    @Transactional(readOnly = true)
    public List<RedeResponse> listarRedes() {
        return redeRepo.findAll().stream().map(this::toRedeResponse).collect(Collectors.toList());
    }

    @Transactional
    public RedeResponse criarRede(RedeRequest req) {
        Rede r = new Rede();
        r.setNome(req.getNome());
        r.setBandeira(req.getBandeira());
        r.setAnyDeskSenha(req.getAnyDeskSenha());
        return toRedeResponse(redeRepo.save(r));
    }

    @Transactional
    public PostoResponse criarPosto(PostoRequest req) {
        Rede rede = redeRepo.findById(req.getRedeId())
            .orElseThrow(() -> new IllegalArgumentException("Rede nao encontrada: " + req.getRedeId()));
        Posto p = new Posto();
        p.setNome(req.getNome());
        p.setEndereco(req.getEndereco());
        p.setCidade(req.getCidade());
        p.setAnyDeskId(req.getAnyDeskId());
        p.setAnyDeskSenha(req.getAnyDeskSenha());
        p.setSistemaInstalado(req.getSistemaInstalado() != null ? req.getSistemaInstalado() : "QualityPos");
        p.setObservacoes(req.getObservacoes());
        p.setRede(rede);
        return toPostoResponse(postoRepo.save(p));
    }

    @Transactional
    public void excluirRede(Long id) {
        Rede rede = redeRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Rede nao encontrada: " + id));
        List<Long> postoIds = rede.getPostos().stream().map(Posto::getId).collect(Collectors.toList());
        if (!postoIds.isEmpty()) {
            chamadoRepo.desvincularPostos(postoIds);
        }
        redeRepo.delete(rede);
    }

    @Transactional
    public void excluirPosto(Long id) {
        chamadoRepo.desvincularPostos(List.of(id));
        postoRepo.deleteById(id);
    }

    @Transactional
    public PostoResponse atualizarPosto(Long postoId, PostoUpdateRequest req) {
        Posto p = postoRepo.findById(postoId)
            .orElseThrow(() -> new IllegalArgumentException("Posto nao encontrado: " + postoId));
        if (req.getNome() != null && !req.getNome().isBlank()) p.setNome(req.getNome());
        p.setAnyDeskId(req.getAnyDeskId());
        p.setAnyDeskSenha(req.getAnyDeskSenha());
        return toPostoResponse(postoRepo.save(p));
    }

    @Transactional
    public PostoResponse atualizarOnline(Long postoId, boolean online) {
        Posto p = postoRepo.findById(postoId)
            .orElseThrow(() -> new IllegalArgumentException("Posto nao encontrado: " + postoId));
        p.setOnline(online);
        return toPostoResponse(postoRepo.save(p));
    }

    private RedeResponse toRedeResponse(Rede r) {
        RedeResponse resp = new RedeResponse();
        resp.setId(r.getId());
        resp.setNome(r.getNome());
        resp.setBandeira(r.getBandeira());
        resp.setAnyDeskSenha(r.getAnyDeskSenha());
        resp.setAtiva(r.getAtiva());
        List<PostoResponse> postos = r.getPostos().stream()
            .map(this::toPostoResponse).collect(Collectors.toList());
        resp.setPostos(postos);
        resp.setTotalPostos(postos.size());
        return resp;
    }

    private PostoResponse toPostoResponse(Posto p) {
        PostoResponse resp = new PostoResponse();
        resp.setId(p.getId());
        resp.setNome(p.getNome());
        resp.setEndereco(p.getEndereco());
        resp.setCidade(p.getCidade());
        resp.setAnyDeskId(p.getAnyDeskId());
        resp.setAnyDeskSenha(p.getAnyDeskSenha());
        resp.setSistemaInstalado(p.getSistemaInstalado());
        resp.setOnline(p.getOnline());
        if (p.getRede() != null) {
            resp.setRedeId(p.getRede().getId());
            resp.setRedeNome(p.getRede().getNome());
        }
        return resp;
    }
}
