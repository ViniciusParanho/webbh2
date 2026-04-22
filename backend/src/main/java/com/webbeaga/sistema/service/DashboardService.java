package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.RegistroPonto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UsuarioRepository usuarioRepo;
    private final EventoRepository eventoRepo;
    private final RedeRepository redeRepo;
    private final RegistroPontoRepository pontoRepo;
    private final ChamadoRepository chamadoRepo;

    public DashboardService(UsuarioRepository ur, EventoRepository er,
                            RedeRepository rr, RegistroPontoRepository pr,
                            ChamadoRepository cr) {
        this.usuarioRepo = ur;
        this.eventoRepo  = er;
        this.redeRepo    = rr;
        this.pontoRepo   = pr;
        this.chamadoRepo = cr;
    }

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = agora.toLocalDate();
        List<Usuario> funcionarios = usuarioRepo.findByAtivoTrue();
        List<RegistroPonto> pontosHoje = pontoRepo.findByData(hoje);

        // Presentes hoje = quem tem entrada registrada
        int presentesHoje = (int) pontosHoje.stream()
            .filter(p -> p.getHoraEntrada() != null).count();

        // Monta lista ponto hoje + guarda hora de entrada para cálculo de ociosidade
        List<PontoHojeItem> pontoHojeList = new ArrayList<>();
        Map<Long, LocalTime> horaEntradaMap = new HashMap<>();
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
                if (p.getHoraSaida() == null) {
                    horaEntradaMap.put(u.getId(), p.getHoraEntrada());
                }
            } else {
                item.setStatus("AUSENTE");
            }
            pontoHojeList.add(item);
        }

        // Mapa de chamados abertos por usuário
        Map<Long, Integer> chamadosAbertosMap = new HashMap<>();
        for (Object[] row : chamadoRepo.findChamadosAbertosCountPorUsuario()) {
            chamadosAbertosMap.put((Long) row[0], ((Number) row[1]).intValue());
        }
        for (PontoHojeItem item : pontoHojeList) {
            Integer count = chamadosAbertosMap.get(item.getUsuarioId());
            if (count != null && count > 0) item.setChamadosAbertos(count);
        }

        // Calcula tempo ocioso: online (sem saída) + sem chamado aberto
        List<Long> emAtendimentoIds = new java.util.ArrayList<>(chamadosAbertosMap.keySet());
        List<Long> onlineOciososIds = horaEntradaMap.keySet().stream()
            .filter(id -> !emAtendimentoIds.contains(id))
            .collect(Collectors.toList());

        Map<Long, LocalDateTime> ultimoChamadoMap = new HashMap<>();
        if (!onlineOciososIds.isEmpty()) {
            List<Object[]> rows = chamadoRepo.findUltimoInicioChamadoHoje(onlineOciososIds, hoje.atStartOfDay());
            for (Object[] row : rows) {
                ultimoChamadoMap.put((Long) row[0], (LocalDateTime) row[1]);
            }
        }

        for (PontoHojeItem item : pontoHojeList) {
            Long uid = item.getUsuarioId();
            if (!horaEntradaMap.containsKey(uid) || emAtendimentoIds.contains(uid)) continue;
            LocalDateTime entradaDt = hoje.atTime(horaEntradaMap.get(uid));
            LocalDateTime referencia = ultimoChamadoMap.getOrDefault(uid, entradaDt);
            if (referencia.isBefore(entradaDt)) referencia = entradaDt;
            long minutos = Duration.between(referencia, agora).toMinutes();
            item.setMinutosOcioso(Math.max(0, minutos));
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
