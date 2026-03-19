package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Erro;
import com.webbeaga.sistema.entity.ErroAnexo;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.ErroAnexoRepository;
import com.webbeaga.sistema.repository.ErroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ErroService {

    private static final String UPLOAD_DIR = "uploads/erros/";

    private final ErroRepository erroRepo;
    private final ErroAnexoRepository anexoRepo;

    public ErroService(ErroRepository erroRepo, ErroAnexoRepository anexoRepo) {
        this.erroRepo  = erroRepo;
        this.anexoRepo = anexoRepo;
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
    public AnexoResponse salvarAnexo(Long erroId, MultipartFile file, ErroAnexo.TipoAnexo tipo) {
        Erro erro = erroRepo.findById(erroId)
            .orElseThrow(() -> new IllegalArgumentException("Erro não encontrado"));

        try {
            Path dir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(dir);

            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }
            String nomeArquivo = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), dir.resolve(nomeArquivo));

            ErroAnexo anexo = new ErroAnexo();
            anexo.setErro(erro);
            anexo.setNomeArquivo(nomeArquivo);
            anexo.setNomeOriginal(original);
            anexo.setContentType(file.getContentType());
            anexo.setTipo(tipo);
            anexo = anexoRepo.save(anexo);

            return toAnexoResponse(anexo);
        } catch (IOException ex) {
            throw new IllegalStateException("Falha ao salvar arquivo: " + ex.getMessage());
        }
    }

    @Transactional
    public void excluir(Long id) {
        Erro e = erroRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Erro não encontrado"));
        // delete files from disk
        e.getAnexos().forEach(a -> {
            try { Files.deleteIfExists(Paths.get(UPLOAD_DIR + a.getNomeArquivo())); }
            catch (IOException ignored) {}
        });
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
        List<AnexoResponse> bug = e.getAnexos().stream()
            .filter(a -> a.getTipo() == ErroAnexo.TipoAnexo.BUG)
            .map(this::toAnexoResponse).collect(Collectors.toList());
        List<AnexoResponse> sol = e.getAnexos().stream()
            .filter(a -> a.getTipo() == ErroAnexo.TipoAnexo.SOLUCAO)
            .map(this::toAnexoResponse).collect(Collectors.toList());
        r.setAnexosBug(bug);
        r.setAnexosSolucao(sol);
        return r;
    }

    private AnexoResponse toAnexoResponse(ErroAnexo a) {
        AnexoResponse ar = new AnexoResponse();
        ar.setId(a.getId());
        ar.setNomeOriginal(a.getNomeOriginal());
        ar.setContentType(a.getContentType());
        ar.setUrl("/uploads/erros/" + a.getNomeArquivo());
        ar.setTipo(a.getTipo().name());
        return ar;
    }
}
