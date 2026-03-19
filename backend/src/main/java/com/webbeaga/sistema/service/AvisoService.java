package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.entity.Aviso;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.AvisoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AvisoService {

    private final AvisoRepository avisoRepo;
    private final UsuarioRepository usuarioRepo;

    public AvisoService(AvisoRepository ar, UsuarioRepository ur) {
        this.avisoRepo   = ar;
        this.usuarioRepo = ur;
    }

    @Transactional(readOnly = true)
    public List<DTOs.AvisoResponse> listarTodos() {
        return avisoRepo.findAllByOrderByCriadoEmDesc().stream()
                .map(this::toResponse).toList();
    }

    @Transactional
    public DTOs.AvisoResponse criar(DTOs.AvisoRequest req, String username) {
        Usuario user = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Aviso a = new Aviso();
        a.setTitulo(req.getTitulo());
        a.setMensagem(req.getMensagem());
        a.setCriadoPor(user);
        return toResponse(avisoRepo.save(a));
    }

    @Transactional
    public void excluir(Long id) {
        avisoRepo.deleteById(id);
    }

    private DTOs.AvisoResponse toResponse(Aviso a) {
        DTOs.AvisoResponse r = new DTOs.AvisoResponse();
        r.setId(a.getId());
        r.setTitulo(a.getTitulo());
        r.setMensagem(a.getMensagem());
        r.setCriadoPorNome(a.getCriadoPor() != null ? a.getCriadoPor().getNomeCompleto() : "Admin");
        r.setCriadoEm(a.getCriadoEm());
        return r;
    }
}
