package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Evento;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.EventoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepo;
    private final UsuarioRepository usuarioRepo;

    public EventoService(EventoRepository eventoRepo, UsuarioRepository usuarioRepo) {
        this.eventoRepo  = eventoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public EventoResponse criar(EventoRequest req, Usuario criadoPor) {
        Evento ev = new Evento();
        ev.setTipo(req.getTipo());
        ev.setRede(req.getRede());
        ev.setPosto(req.getPosto());
        ev.setDataEvento(req.getDataEvento());
        ev.setHoraEvento(req.getHoraEvento());
        ev.setObservacao(req.getObservacao());
        ev.setCriadoPor(criadoPor);

        if (req.getResponsaveisIds() != null && !req.getResponsaveisIds().isEmpty()) {
            List<Usuario> resps = usuarioRepo.findAllById(req.getResponsaveisIds());
            ev.setResponsaveis(resps);
        }

        return EventoResponse.from(eventoRepo.save(ev));
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarTodos() {
        return eventoRepo.findAllByOrderByDataEventoAscHoraEventoAsc()
            .stream().map(EventoResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarProximos() {
        return eventoRepo.findProximos(LocalDate.now())
            .stream().map(EventoResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return eventoRepo.findByDataEventoBetweenOrderByDataEventoAscHoraEventoAsc(inicio, fim)
            .stream().map(EventoResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public EventoResponse atualizarStatus(Long id, Evento.StatusEvento novoStatus) {
        Evento ev = eventoRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Evento nao encontrado: " + id));
        ev.setStatus(novoStatus);
        return EventoResponse.from(eventoRepo.save(ev));
    }

    @Transactional
    public void excluir(Long id) {
        if (!eventoRepo.existsById(id))
            throw new IllegalArgumentException("Evento nao encontrado: " + id);
        eventoRepo.deleteById(id);
    }

    public long contarAgendados() {
        return eventoRepo.countByStatus(Evento.StatusEvento.AGENDADO);
    }
}
