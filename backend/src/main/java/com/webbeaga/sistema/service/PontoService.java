package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.RegistroPonto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.RegistroPontoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoService {

    private final RegistroPontoRepository pontoRepo;

    public PontoService(RegistroPontoRepository pontoRepo) {
        this.pontoRepo = pontoRepo;
    }

    @Transactional
    public PontoResponse baterPonto(Usuario usuario, String tipo) {
        LocalDate hoje = LocalDate.now();
        RegistroPonto ponto = pontoRepo.findByUsuarioIdAndData(usuario.getId(), hoje)
            .orElseGet(() -> {
                RegistroPonto novo = new RegistroPonto();
                novo.setUsuario(usuario);
                novo.setData(hoje);
                novo.setStatus(RegistroPonto.StatusPonto.EM_CURSO);
                return novo;
            });

        LocalTime agora = LocalTime.now().withSecond(0).withNano(0);

        switch (tipo.toUpperCase()) {
            case "ENTRADA" -> {
                if (ponto.getHoraEntrada() != null)
                    throw new IllegalStateException("Entrada ja registrada hoje");
                ponto.setHoraEntrada(agora);
                ponto.setStatus(RegistroPonto.StatusPonto.EM_CURSO);
            }
            case "SAIDA_ALMOCO" -> {
                if (ponto.getHoraEntrada() == null)
                    throw new IllegalStateException("Registre a entrada primeiro");
                if (ponto.getHoraSaidaAlmoco() != null)
                    throw new IllegalStateException("Saida almoco ja registrada");
                ponto.setHoraSaidaAlmoco(agora);
            }
            case "RETORNO_ALMOCO" -> {
                if (ponto.getHoraSaidaAlmoco() == null)
                    throw new IllegalStateException("Registre a saida do almoco primeiro");
                if (ponto.getHoraRetornoAlmoco() != null)
                    throw new IllegalStateException("Retorno almoco ja registrado");
                ponto.setHoraRetornoAlmoco(agora);
            }
            case "SAIDA" -> {
                if (ponto.getHoraEntrada() == null)
                    throw new IllegalStateException("Registre a entrada primeiro");
                if (ponto.getHoraSaida() != null)
                    throw new IllegalStateException("Saida ja registrada hoje");
                ponto.setHoraSaida(agora);
                ponto.recalcular();
            }
            default -> throw new IllegalArgumentException("Tipo invalido: " + tipo);
        }

        return PontoResponse.from(pontoRepo.save(ponto));
    }

    @Transactional(readOnly = true)
    public PontoResponse getPontoHoje(Long usuarioId) {
        return pontoRepo.findByUsuarioIdAndData(usuarioId, LocalDate.now())
            .map(PontoResponse::from)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PontoResponse> getHistorico(Long usuarioId, int ano, int mes) {
        return pontoRepo.findByUsuarioIdAndMes(usuarioId, ano, mes)
            .stream().map(PontoResponse::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PontoResponse> getPorPeriodo(Long usuarioId, LocalDate inicio, LocalDate fim) {
        return pontoRepo.findByUsuarioIdAndDataBetweenOrderByData(usuarioId, inicio, fim)
            .stream().map(PontoResponse::from).collect(Collectors.toList());
    }
}
