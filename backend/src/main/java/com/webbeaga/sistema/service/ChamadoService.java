package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs;
import com.webbeaga.sistema.entity.Chamado;
import com.webbeaga.sistema.entity.Posto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.ChamadoRepository;
import com.webbeaga.sistema.repository.PostoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepo;
    private final UsuarioRepository usuarioRepo;
    private final PostoRepository postoRepo;

    public ChamadoService(ChamadoRepository cr, UsuarioRepository ur, PostoRepository pr) {
        this.chamadoRepo  = cr;
        this.usuarioRepo  = ur;
        this.postoRepo    = pr;
    }

    @Transactional(readOnly = true)
    public List<Long> listarUsuariosEmAtendimento() {
        return chamadoRepo.findUsuarioIdsEmAtendimento();
    }

    @Transactional(readOnly = true)
    public List<DTOs.ChamadoResponse> listarMeus(String username) {
        Usuario u = usuarioRepo.findByUsername(username).orElseThrow();
        return chamadoRepo.findByUsuarioId(u.getId()).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<DTOs.ChamadoResponse> listarTodos() {
        return chamadoRepo.findAllByOrderByDataInicioDesc().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<DTOs.ChamadoResponse> listarProdutividade(Long usuarioId, LocalDate inicio, LocalDate fim) {
        LocalDateTime inicioTs = inicio.atStartOfDay();
        LocalDateTime fimTs    = fim.plusDays(1).atStartOfDay();
        return chamadoRepo.findByUsuarioId(usuarioId).stream()
                .filter(c -> c.getDataInicio() != null
                        && !c.getDataInicio().isBefore(inicioTs)
                        && c.getDataInicio().isBefore(fimTs))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DTOs.ChamadoResponse criar(DTOs.ChamadoRequest req, String username) {
        Usuario u = usuarioRepo.findByUsername(username).orElseThrow();
        Chamado c = new Chamado();
        c.setTipo(Chamado.TipoChamado.valueOf(req.getTipo()));
        c.setDescricao(req.getDescricao());
        c.setUsuario(u);
        if (req.getPostoId() != null) {
            postoRepo.findById(req.getPostoId()).ifPresent(c::setPosto);
        }
        c.setDataInicio(LocalDateTime.now());
        c.setStatus(Chamado.StatusChamado.ABERTO);
        return toResponse(chamadoRepo.save(c));
    }

    @Transactional
    public DTOs.ChamadoResponse finalizar(Long id, String username) {
        Chamado c = chamadoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado"));
        if (!c.getUsuario().getUsername().equals(username))
            throw new RuntimeException("Sem permissão");
        LocalDateTime fim = LocalDateTime.now();
        c.setDataFim(fim);
        c.setDuracaoSegundos(ChronoUnit.SECONDS.between(c.getDataInicio(), fim));
        c.setStatus(Chamado.StatusChamado.CONCLUIDO);
        return toResponse(chamadoRepo.save(c));
    }

    @Transactional(readOnly = true)
    public List<DTOs.RankingItem> rankingDiario() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fim    = inicio.plusDays(1);
        Map<Long, Long> contagemPorUsuario = chamadoRepo.findAllByOrderByDataInicioDesc().stream()
                .filter(c -> c.getStatus() == Chamado.StatusChamado.CONCLUIDO
                        && c.getDataInicio() != null
                        && !c.getDataInicio().isBefore(inicio)
                        && c.getDataInicio().isBefore(fim))
                .collect(Collectors.groupingBy(c -> c.getUsuario().getId(), Collectors.counting()));
        return usuarioRepo.findAll().stream()
                .map(u -> {
                    DTOs.RankingItem r = new DTOs.RankingItem();
                    r.setUsuarioId(u.getId());
                    r.setUsuarioNome(u.getNomeCompleto());
                    r.setQuantidade(contagemPorUsuario.getOrDefault(u.getId(), 0L).intValue());
                    return r;
                })
                .sorted(Comparator.comparingInt(DTOs.RankingItem::getQuantidade).reversed())
                .toList();
    }

    @Transactional
    public void excluir(Long id) {
        chamadoRepo.deleteById(id);
    }

    private DTOs.ChamadoResponse toResponse(Chamado c) {
        DTOs.ChamadoResponse r = new DTOs.ChamadoResponse();
        r.setId(c.getId());
        r.setTipo(c.getTipo().name());
        r.setTipoDescricao(c.getTipo().getDescricao());
        r.setDescricao(c.getDescricao());
        r.setStatus(c.getStatus().name());
        r.setUsuarioNome(c.getUsuario().getNomeCompleto());
        r.setUsuarioId(c.getUsuario().getId());
        if (c.getPosto() != null) {
            r.setPostoNome(c.getPosto().getNome());
            r.setPostoId(c.getPosto().getId());
        }
        r.setDataInicio(c.getDataInicio());
        r.setDataFim(c.getDataFim());
        r.setDuracaoSegundos(c.getDuracaoSegundos());
        return r;
    }
}
