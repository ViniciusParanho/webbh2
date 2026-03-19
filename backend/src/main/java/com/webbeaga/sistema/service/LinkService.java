package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.entity.Link;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.LinkRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LinkService {

    private final LinkRepository linkRepo;
    private final UsuarioRepository usuarioRepo;

    public LinkService(LinkRepository lr, UsuarioRepository ur) {
        this.linkRepo   = lr;
        this.usuarioRepo = ur;
    }

    @Transactional(readOnly = true)
    public List<DTOs.LinkResponse> listarTodos() {
        return linkRepo.findAllByOrderByCriadoEmAsc().stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public DTOs.LinkResponse criar(DTOs.LinkRequest req, String username) {
        Usuario user = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Link link = new Link();
        link.setTitulo(req.getTitulo());
        link.setUrl(req.getUrl());
        link.setCriadoPor(user);
        return toResponse(linkRepo.save(link));
    }

    @Transactional
    public void excluir(Long id) {
        linkRepo.deleteById(id);
    }

    private DTOs.LinkResponse toResponse(Link l) {
        DTOs.LinkResponse r = new DTOs.LinkResponse();
        r.setId(l.getId());
        r.setTitulo(l.getTitulo());
        r.setUrl(l.getUrl());
        r.setCriadoPorNome(l.getCriadoPor() != null ? l.getCriadoPor().getNomeCompleto() : "Sistema");
        r.setCriadoEm(l.getCriadoEm());
        return r;
    }
}
