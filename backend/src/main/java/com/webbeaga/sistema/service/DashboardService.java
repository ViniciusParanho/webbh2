package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.RegistroPonto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepo;
    private final EventoRepository eventoRepo;
    private final RedeRepository redeRepo;
    private final RegistroPontoRepository pontoRepo;

    public DashboardService(UsuarioRepository ur, EventoRepository er,
                            RedeRepository rr, RegistroPontoRepository pr) {
        this.usuarioRepo = ur;
        this.eventoRepo  = er;
        this.redeRepo    = rr;
        this.pontoRepo   = pr;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        List<Usuario> funcionarios = usuarioRepo.findByAtivoTrue();
        List<RegistroPonto> pontosHoje = pontoRepo.findByData(LocalDate.now());

        // Presentes hoje = quem tem entrada registrada
        int presentesHoje = (int) pontosHoje.stream()
            .filter(p -> p.getHoraEntrada() != null).count();

        // Monta lista ponto hoje
        List<PontoHojeItem> pontoHojeList = new ArrayList<>();
        for (Usuario u : funcionarios) {
            PontoHojeItem item = new PontoHojeItem();
            item.setUsuarioId(u.getId());
            item.setNome(u.getNomeCompleto());
            RegistroPonto p = pontosHoje.stream()
                .filter(x -> x.getUsuario() != null && x.getUsuario().getId().equals(u.getId()))
                .findFirst().orElse(null);
            if (p != null && p.getHoraEntrada() != null) {
                item.setStatus(p.getStatus() != null ? p.getStatus().name() : "EM_CURSO");
                item.setEntrada(p.getHoraEntrada().toString());
                item.setSaida(p.getHoraSaida() != null ? p.getHoraSaida().toString() : null);
            } else {
                item.setStatus("AUSENTE");
            }
            pontoHojeList.add(item);
        }

        List<EventoResponse> proximas = eventoRepo.findProximos(LocalDate.now()).stream()
            .limit(5).map(EventoResponse::from).collect(Collectors.toList());

        DashboardResponse r = new DashboardResponse();
        r.setTotalFuncionarios(funcionarios.size());
        r.setPresentesHoje(presentesHoje);
        r.setInstalacoesPendentes(eventoRepo.countByStatus(
            com.webbeaga.sistema.entity.Evento.StatusEvento.AGENDADO));
        r.setRedesCadastradas(redeRepo.count());
        r.setProximasInstalacoes(proximas);
        r.setPontoHoje(pontoHojeList);
        return r;
    }
}
