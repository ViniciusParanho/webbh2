package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.RegistroPonto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.RegistroPontoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    private final UsuarioRepository usuarioRepo;
    private final RegistroPontoRepository pontoRepo;

    public RelatorioService(UsuarioRepository ur, RegistroPontoRepository pr) {
        this.usuarioRepo = ur;
        this.pontoRepo   = pr;
    }

    @Transactional(readOnly = true)
    public RelatorioMesResponse getRelatorioUsuario(Long usuarioId, int ano, int mes) {
        Usuario u = usuarioRepo.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + usuarioId));
        return buildRelatorio(u, ano, mes);
    }

    @Transactional(readOnly = true)
    public List<RelatorioMesResponse> getRelatorioEquipe(int ano, int mes) {
        return usuarioRepo.findByAtivoTrue().stream()
            .map(u -> buildRelatorio(u, ano, mes))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResumoEquipeResponse getResumoEquipe(int ano, int mes) {
        List<Usuario> ativos = usuarioRepo.findByAtivoTrue();
        int totalMin = 0, totalExtras = 0, totalAus = 0;
        List<ResumoFuncResponse> lista = new ArrayList<>();

        for (Usuario u : ativos) {
            RelatorioMesResponse rel = buildRelatorio(u, ano, mes);
            totalMin    += rel.getTotalMinutos();
            totalExtras += rel.getMinutosExtras();
            totalAus    += rel.getAusencias();

            ResumoFuncResponse f = new ResumoFuncResponse();
            f.setId(u.getId());
            f.setNome(u.getNomeCompleto());
            f.setCargo(u.getCargo());
            f.setHorasTrabalhadas(rel.getTotalMinutos() / 60);
            f.setMinutosExtras(rel.getMinutosExtras());
            f.setDiasTrabalhados(rel.getDiasTrabalhados());
            f.setAusencias(rel.getAusencias());
            f.setStatus(rel.getAusencias() > 3 ? "IRREGULAR"
                       : rel.getAusencias() > 1 ? "ATENCAO" : "OK");
            lista.add(f);
        }

        ResumoEquipeResponse r = new ResumoEquipeResponse();
        r.setAno(ano); r.setMes(mes);
        r.setTotalFuncionarios(ativos.size());
        r.setTotalHorasEquipe(totalMin / 60);
        r.setTotalExtrasMinutos(totalExtras);
        r.setTotalAusencias(totalAus);
        r.setFuncionarios(lista);
        return r;
    }

    private RelatorioMesResponse buildRelatorio(Usuario u, int ano, int mes) {
        List<RegistroPonto> registros = pontoRepo.findByUsuarioIdAndMes(u.getId(), ano, mes);
        int totalMin = 0, extrasMin = 0, devendoMin = 0;
        long diasTrab = 0;
        for (RegistroPonto r : registros) {
            if (r.getTotalMinutos()   != null) totalMin   += r.getTotalMinutos();
            if (r.getMinutosExtras()  != null) extrasMin  += r.getMinutosExtras();
            if (r.getMinutosDevendo() != null) devendoMin += r.getMinutosDevendo();
            if (r.getHoraSaida()      != null) diasTrab++;
        }
        int diasUteis = calcDiasUteis(ano, mes);

        RelatorioMesResponse r = new RelatorioMesResponse();
        r.setNomeUsuario(u.getNomeCompleto());
        r.setCargo(u.getCargo());
        r.setAno(ano); r.setMes(mes);
        r.setTotalMinutos(totalMin);
        r.setMinutosExtras(extrasMin);
        r.setMinutosDevendo(devendoMin);
        r.setDiasTrabalhados((int) diasTrab);
        r.setAusencias((int) Math.max(0, diasUteis - diasTrab));
        r.setRegistros(registros.stream().map(PontoResponse::from).collect(Collectors.toList()));
        return r;
    }

    private int calcDiasUteis(int ano, int mes) {
        LocalDate d    = LocalDate.of(ano, mes, 1);
        LocalDate fim  = d.withDayOfMonth(d.lengthOfMonth());
        LocalDate hoje = LocalDate.now();
        if (fim.isAfter(hoje)) fim = hoje;
        int count = 0;
        // Segunda(1) a Sábado(6) são dias úteis; Domingo(7) não
        while (!d.isAfter(fim)) {
            if (d.getDayOfWeek().getValue() <= 6) count++;
            d = d.plusDays(1);
        }
        return count;
    }

    // Inner DTOs
    public static class ResumoEquipeResponse {
        private int ano, mes, totalFuncionarios, totalHorasEquipe, totalExtrasMinutos, totalAusencias;
        private List<ResumoFuncResponse> funcionarios;
        public int getAno()                         { return ano; }
        public int getMes()                         { return mes; }
        public int getTotalFuncionarios()           { return totalFuncionarios; }
        public int getTotalHorasEquipe()            { return totalHorasEquipe; }
        public int getTotalExtrasMinutos()          { return totalExtrasMinutos; }
        public int getTotalAusencias()              { return totalAusencias; }
        public List<ResumoFuncResponse> getFuncionarios(){ return funcionarios; }
        public void setAno(int v)                         { this.ano = v; }
        public void setMes(int v)                         { this.mes = v; }
        public void setTotalFuncionarios(int v)           { this.totalFuncionarios = v; }
        public void setTotalHorasEquipe(int v)            { this.totalHorasEquipe = v; }
        public void setTotalExtrasMinutos(int v)          { this.totalExtrasMinutos = v; }
        public void setTotalAusencias(int v)              { this.totalAusencias = v; }
        public void setFuncionarios(List<ResumoFuncResponse> v){ this.funcionarios = v; }
    }

    public static class ResumoFuncResponse {
        private Long id;
        private String nome, cargo, status;
        private int horasTrabalhadas, minutosExtras, diasTrabalhados, ausencias;
        public Long getId()               { return id; }
        public String getNome()           { return nome; }
        public String getCargo()          { return cargo; }
        public String getStatus()         { return status; }
        public int getHorasTrabalhadas()  { return horasTrabalhadas; }
        public int getMinutosExtras()     { return minutosExtras; }
        public int getDiasTrabalhados()   { return diasTrabalhados; }
        public int getAusencias()         { return ausencias; }
        public void setId(Long v)               { this.id = v; }
        public void setNome(String v)           { this.nome = v; }
        public void setCargo(String v)          { this.cargo = v; }
        public void setStatus(String v)         { this.status = v; }
        public void setHorasTrabalhadas(int v)  { this.horasTrabalhadas = v; }
        public void setMinutosExtras(int v)     { this.minutosExtras = v; }
        public void setDiasTrabalhados(int v)   { this.diasTrabalhados = v; }
        public void setAusencias(int v)         { this.ausencias = v; }
    }
}
