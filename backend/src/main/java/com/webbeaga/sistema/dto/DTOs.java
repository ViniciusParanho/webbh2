package com.webbeaga.sistema.dto;

import com.webbeaga.sistema.entity.Evento;
import com.webbeaga.sistema.entity.RegistroPonto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DTOs {

    // ─── Auth ────────────────────────────────────────────────

    public static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
        public String getUsername() { return username; }
        public void setUsername(String v) { this.username = v; }
        public String getPassword() { return password; }
        public void setPassword(String v) { this.password = v; }
    }

    public static class LoginResponse {
        private Long id;
        private String token, username, nomeCompleto, cargo, role;
        public Long getId()            { return id; }
        public String getToken()       { return token; }
        public String getUsername()    { return username; }
        public String getNomeCompleto(){ return nomeCompleto; }
        public String getCargo()       { return cargo; }
        public String getRole()        { return role; }
        public void setId(Long v)            { this.id = v; }
        public void setToken(String v)       { this.token = v; }
        public void setUsername(String v)    { this.username = v; }
        public void setNomeCompleto(String v){ this.nomeCompleto = v; }
        public void setCargo(String v)       { this.cargo = v; }
        public void setRole(String v)        { this.role = v; }
    }

    // ─── Usuario ─────────────────────────────────────────────

    public static class UsuarioRequest {
        @NotBlank private String nomeCompleto;
        @NotBlank private String username;
        @NotBlank private String password;
        @NotBlank private String cargo;
        private String role, email, telefone;
        private Integer cargaHorariaDiaria;
        public String getNomeCompleto()        { return nomeCompleto; }
        public String getUsername()            { return username; }
        public String getPassword()            { return password; }
        public String getCargo()               { return cargo; }
        public String getRole()                { return role; }
        public String getEmail()               { return email; }
        public String getTelefone()            { return telefone; }
        public Integer getCargaHorariaDiaria() { return cargaHorariaDiaria; }
        public void setNomeCompleto(String v)        { this.nomeCompleto = v; }
        public void setUsername(String v)            { this.username = v; }
        public void setPassword(String v)            { this.password = v; }
        public void setCargo(String v)               { this.cargo = v; }
        public void setRole(String v)                { this.role = v; }
        public void setEmail(String v)               { this.email = v; }
        public void setTelefone(String v)            { this.telefone = v; }
        public void setCargaHorariaDiaria(Integer v) { this.cargaHorariaDiaria = v; }
    }

    public static class UsuarioResponse {
        private Long id;
        private String username, nomeCompleto, cargo, role, email, telefone;
        private Integer cargaHorariaDiaria, horasMes, minutosExtrasMes, ausenciasMes;
        private Boolean ativo;
        private LocalDateTime criadoEm;
        private String statusPontoHoje;
        public Long getId()                    { return id; }
        public String getUsername()            { return username; }
        public String getNomeCompleto()        { return nomeCompleto; }
        public String getCargo()               { return cargo; }
        public String getRole()                { return role; }
        public String getEmail()               { return email; }
        public String getTelefone()            { return telefone; }
        public Integer getCargaHorariaDiaria() { return cargaHorariaDiaria; }
        public Integer getHorasMes()           { return horasMes; }
        public Integer getMinutosExtrasMes()   { return minutosExtrasMes; }
        public Integer getAusenciasMes()       { return ausenciasMes; }
        public Boolean getAtivo()              { return ativo; }
        public LocalDateTime getCriadoEm()     { return criadoEm; }
        public String getStatusPontoHoje()     { return statusPontoHoje; }
        public void setId(Long v)                      { this.id = v; }
        public void setUsername(String v)              { this.username = v; }
        public void setNomeCompleto(String v)          { this.nomeCompleto = v; }
        public void setCargo(String v)                 { this.cargo = v; }
        public void setRole(String v)                  { this.role = v; }
        public void setEmail(String v)                 { this.email = v; }
        public void setTelefone(String v)              { this.telefone = v; }
        public void setCargaHorariaDiaria(Integer v)   { this.cargaHorariaDiaria = v; }
        public void setHorasMes(Integer v)             { this.horasMes = v; }
        public void setMinutosExtrasMes(Integer v)     { this.minutosExtrasMes = v; }
        public void setAusenciasMes(Integer v)         { this.ausenciasMes = v; }
        public void setAtivo(Boolean v)                { this.ativo = v; }
        public void setCriadoEm(LocalDateTime v)       { this.criadoEm = v; }
        public void setStatusPontoHoje(String v)       { this.statusPontoHoje = v; }
    }

    // ─── Ponto ───────────────────────────────────────────────

    public static class BaterPontoRequest {
        @NotBlank private String tipo;
        public String getTipo() { return tipo; }
        public void setTipo(String v) { this.tipo = v; }
    }

    public static class PontoResponse {
        private Long id, usuarioId;
        private LocalDate data;
        private String horaEntrada, horaSaidaAlmoco, horaRetornoAlmoco, horaSaida;
        private Integer totalMinutos, minutosExtras, minutosDevendo;
        private String status, observacao, usuarioNome;

        public PontoResponse() {}

        public static PontoResponse from(RegistroPonto r) {
            PontoResponse p = new PontoResponse();
            p.id            = r.getId();
            p.data          = r.getData();
            p.horaEntrada       = r.getHoraEntrada()      != null ? r.getHoraEntrada().toString()      : null;
            p.horaSaidaAlmoco   = r.getHoraSaidaAlmoco()  != null ? r.getHoraSaidaAlmoco().toString()  : null;
            p.horaRetornoAlmoco = r.getHoraRetornoAlmoco()!= null ? r.getHoraRetornoAlmoco().toString(): null;
            p.horaSaida         = r.getHoraSaida()        != null ? r.getHoraSaida().toString()        : null;
            p.totalMinutos   = r.getTotalMinutos();
            p.minutosExtras  = r.getMinutosExtras();
            p.minutosDevendo = r.getMinutosDevendo();
            p.status         = r.getStatus() != null ? r.getStatus().name() : null;
            p.observacao    = r.getObservacao();
            if (r.getUsuario() != null) {
                p.usuarioId  = r.getUsuario().getId();
                p.usuarioNome= r.getUsuario().getNomeCompleto();
            }
            return p;
        }

        public Long getId()                  { return id; }
        public Long getUsuarioId()           { return usuarioId; }
        public LocalDate getData()           { return data; }
        public String getHoraEntrada()       { return horaEntrada; }
        public String getHoraSaidaAlmoco()   { return horaSaidaAlmoco; }
        public String getHoraRetornoAlmoco() { return horaRetornoAlmoco; }
        public String getHoraSaida()         { return horaSaida; }
        public Integer getTotalMinutos()     { return totalMinutos; }
        public Integer getMinutosExtras()    { return minutosExtras; }
        public Integer getMinutosDevendo()   { return minutosDevendo; }
        public String getStatus()            { return status; }
        public String getObservacao()        { return observacao; }
        public String getUsuarioNome()       { return usuarioNome; }
        public void setData(LocalDate v)     { this.data = v; }
        public void setStatus(String v)      { this.status = v; }
        public void setUsuarioId(Long v)     { this.usuarioId = v; }
        public void setUsuarioNome(String v) { this.usuarioNome = v; }
    }

    // ─── Evento ──────────────────────────────────────────────

    public static class EventoRequest {
        @NotNull  private Evento.TipoEvento tipo;
        @NotBlank private String rede;
        @NotBlank private String posto;
        @NotNull  private LocalDate dataEvento;
        private LocalTime horaEvento;
        private List<Long> responsaveisIds;
        private String observacao;
        public Evento.TipoEvento getTipo()     { return tipo; }
        public String getRede()                { return rede; }
        public String getPosto()               { return posto; }
        public LocalDate getDataEvento()       { return dataEvento; }
        public LocalTime getHoraEvento()       { return horaEvento; }
        public List<Long> getResponsaveisIds() { return responsaveisIds; }
        public String getObservacao()          { return observacao; }
        public void setTipo(Evento.TipoEvento v)    { this.tipo = v; }
        public void setRede(String v)               { this.rede = v; }
        public void setPosto(String v)              { this.posto = v; }
        public void setDataEvento(LocalDate v)      { this.dataEvento = v; }
        public void setHoraEvento(LocalTime v)      { this.horaEvento = v; }
        public void setResponsaveisIds(List<Long> v){ this.responsaveisIds = v; }
        public void setObservacao(String v)         { this.observacao = v; }
    }

    public static class EventoStatusRequest {
        @NotNull private Evento.StatusEvento status;
        public Evento.StatusEvento getStatus()      { return status; }
        public void setStatus(Evento.StatusEvento v){ this.status = v; }
    }

    public static class EventoResponse {
        private Long id;
        private String tipo, tipoDescricao, rede, posto;
        private LocalDate dataEvento;
        private String horaEvento, status, observacao;
        private LocalDateTime criadoEm;
        private List<Long> responsaveisIds;
        private List<String> responsaveisNomes;

        public EventoResponse() {}

        public static EventoResponse from(Evento e) {
            EventoResponse r = new EventoResponse();
            r.id            = e.getId();
            r.tipo          = e.getTipo().name();
            r.tipoDescricao = e.getTipo().getDescricao();
            r.rede          = e.getRede();
            r.posto         = e.getPosto();
            r.dataEvento    = e.getDataEvento();
            r.horaEvento    = e.getHoraEvento() != null ? e.getHoraEvento().toString() : null;
            r.status        = e.getStatus().name();
            r.observacao    = e.getObservacao();
            r.criadoEm      = e.getCriadoEm();
            r.responsaveisIds   = e.getResponsaveis().stream().map(u -> u.getId()).collect(java.util.stream.Collectors.toList());
            r.responsaveisNomes = e.getResponsaveis().stream().map(u -> u.getNomeCompleto()).collect(java.util.stream.Collectors.toList());
            return r;
        }

        public Long getId()                      { return id; }
        public String getTipo()                  { return tipo; }
        public String getTipoDescricao()         { return tipoDescricao; }
        public String getRede()                  { return rede; }
        public String getPosto()                 { return posto; }
        public LocalDate getDataEvento()         { return dataEvento; }
        public String getHoraEvento()            { return horaEvento; }
        public String getStatus()                { return status; }
        public String getObservacao()            { return observacao; }
        public LocalDateTime getCriadoEm()       { return criadoEm; }
        public List<Long> getResponsaveisIds()   { return responsaveisIds; }
        public List<String> getResponsaveisNomes(){ return responsaveisNomes; }
    }

    // ─── Rede / Posto ─────────────────────────────────────────

    public static class RedeRequest {
        @NotBlank private String nome;
        private String bandeira;
        private String anyDeskSenha;
        public String getNome()          { return nome; }
        public String getBandeira()      { return bandeira; }
        public String getAnyDeskSenha()  { return anyDeskSenha; }
        public void setNome(String v)          { this.nome = v; }
        public void setBandeira(String v)      { this.bandeira = v; }
        public void setAnyDeskSenha(String v)  { this.anyDeskSenha = v; }
    }

    public static class PostoRequest {
        @NotBlank private String nome;
        @NotNull  private Long redeId;
        private String endereco, cidade, anyDeskId, anyDeskSenha, sistemaInstalado, observacoes;
        public String getNome()             { return nome; }
        public Long getRedeId()             { return redeId; }
        public String getEndereco()         { return endereco; }
        public String getCidade()           { return cidade; }
        public String getAnyDeskId()        { return anyDeskId; }
        public String getAnyDeskSenha()     { return anyDeskSenha; }
        public String getSistemaInstalado() { return sistemaInstalado; }
        public String getObservacoes()      { return observacoes; }
        public void setNome(String v)             { this.nome = v; }
        public void setRedeId(Long v)             { this.redeId = v; }
        public void setEndereco(String v)         { this.endereco = v; }
        public void setCidade(String v)           { this.cidade = v; }
        public void setAnyDeskId(String v)        { this.anyDeskId = v; }
        public void setAnyDeskSenha(String v)     { this.anyDeskSenha = v; }
        public void setSistemaInstalado(String v) { this.sistemaInstalado = v; }
        public void setObservacoes(String v)      { this.observacoes = v; }
    }

    public static class RedeResponse {
        private Long id;
        private String nome, bandeira, anyDeskSenha;
        private Boolean ativa;
        private int totalPostos;
        private List<PostoResponse> postos;
        public Long getId()                { return id; }
        public String getNome()            { return nome; }
        public String getBandeira()        { return bandeira; }
        public String getAnyDeskSenha()    { return anyDeskSenha; }
        public Boolean getAtiva()          { return ativa; }
        public int getTotalPostos()        { return totalPostos; }
        public List<PostoResponse> getPostos(){ return postos; }
        public void setId(Long v)                       { this.id = v; }
        public void setNome(String v)                   { this.nome = v; }
        public void setBandeira(String v)               { this.bandeira = v; }
        public void setAnyDeskSenha(String v)           { this.anyDeskSenha = v; }
        public void setAtiva(Boolean v)                 { this.ativa = v; }
        public void setTotalPostos(int v)               { this.totalPostos = v; }
        public void setPostos(List<PostoResponse> v)    { this.postos = v; }
    }

    public static class PostoResponse {
        private Long id, redeId;
        private String nome, endereco, cidade, anyDeskId, anyDeskSenha, sistemaInstalado, redeNome;
        private Boolean online;
        public Long getId()                { return id; }
        public Long getRedeId()            { return redeId; }
        public String getNome()            { return nome; }
        public String getEndereco()        { return endereco; }
        public String getCidade()          { return cidade; }
        public String getAnyDeskId()       { return anyDeskId; }
        public String getAnyDeskSenha()    { return anyDeskSenha; }
        public String getSistemaInstalado(){ return sistemaInstalado; }
        public String getRedeNome()        { return redeNome; }
        public Boolean getOnline()         { return online; }
        public void setId(Long v)                 { this.id = v; }
        public void setRedeId(Long v)             { this.redeId = v; }
        public void setNome(String v)             { this.nome = v; }
        public void setEndereco(String v)         { this.endereco = v; }
        public void setCidade(String v)           { this.cidade = v; }
        public void setAnyDeskId(String v)        { this.anyDeskId = v; }
        public void setAnyDeskSenha(String v)     { this.anyDeskSenha = v; }
        public void setSistemaInstalado(String v) { this.sistemaInstalado = v; }
        public void setRedeNome(String v)         { this.redeNome = v; }
        public void setOnline(Boolean v)          { this.online = v; }
    }

    // ─── Dashboard ────────────────────────────────────────────

    public static class DashboardResponse {
        private int totalFuncionarios, presentesHoje;
        private long instalacoesPendentes, redesCadastradas;
        private List<EventoResponse> proximasInstalacoes;
        private List<PontoHojeItem> pontoHoje;
        public int getTotalFuncionarios()             { return totalFuncionarios; }
        public int getPresentesHoje()                 { return presentesHoje; }
        public long getInstalacoesPendentes()         { return instalacoesPendentes; }
        public long getRedesCadastradas()             { return redesCadastradas; }
        public List<EventoResponse> getProximasInstalacoes(){ return proximasInstalacoes; }
        public List<PontoHojeItem> getPontoHoje()     { return pontoHoje; }
        public void setTotalFuncionarios(int v)                      { this.totalFuncionarios = v; }
        public void setPresentesHoje(int v)                          { this.presentesHoje = v; }
        public void setInstalacoesPendentes(long v)                  { this.instalacoesPendentes = v; }
        public void setRedesCadastradas(long v)                      { this.redesCadastradas = v; }
        public void setProximasInstalacoes(List<EventoResponse> v)   { this.proximasInstalacoes = v; }
        public void setPontoHoje(List<PontoHojeItem> v)              { this.pontoHoje = v; }
    }

    public static class PontoHojeItem {
        private Long usuarioId;
        private String nome, status, entrada, saida;
        public Long getUsuarioId()  { return usuarioId; }
        public String getNome()     { return nome; }
        public String getStatus()   { return status; }
        public String getEntrada()  { return entrada; }
        public String getSaida()    { return saida; }
        public void setUsuarioId(Long v)  { this.usuarioId = v; }
        public void setNome(String v)     { this.nome = v; }
        public void setStatus(String v)   { this.status = v; }
        public void setEntrada(String v)  { this.entrada = v; }
        public void setSaida(String v)    { this.saida = v; }
    }

    // ─── Relatório ────────────────────────────────────────────

    public static class RelatorioMesResponse {
        private String nomeUsuario, cargo;
        private int ano, mes, totalMinutos, minutosExtras, minutosDevendo, diasTrabalhados, ausencias;
        private List<PontoResponse> registros;
        public String getNomeUsuario()       { return nomeUsuario; }
        public String getCargo()             { return cargo; }
        public int getAno()                  { return ano; }
        public int getMes()                  { return mes; }
        public int getTotalMinutos()         { return totalMinutos; }
        public int getMinutosExtras()        { return minutosExtras; }
        public int getMinutosDevendo()       { return minutosDevendo; }
        public int getDiasTrabalhados()      { return diasTrabalhados; }
        public int getAusencias()            { return ausencias; }
        public List<PontoResponse> getRegistros(){ return registros; }
        public void setNomeUsuario(String v)           { this.nomeUsuario = v; }
        public void setCargo(String v)                 { this.cargo = v; }
        public void setAno(int v)                      { this.ano = v; }
        public void setMes(int v)                      { this.mes = v; }
        public void setTotalMinutos(int v)             { this.totalMinutos = v; }
        public void setMinutosExtras(int v)            { this.minutosExtras = v; }
        public void setMinutosDevendo(int v)           { this.minutosDevendo = v; }
        public void setDiasTrabalhados(int v)          { this.diasTrabalhados = v; }
        public void setAusencias(int v)                { this.ausencias = v; }
        public void setRegistros(List<PontoResponse> v){ this.registros = v; }
    }

    // ─── Erro / Bug ───────────────────────────────────────────

    public static class ErroRequest {
        @NotBlank private String titulo;
        private String descricao, tipoErro;
        public String getTitulo()         { return titulo; }
        public String getDescricao()      { return descricao; }
        public String getTipoErro()       { return tipoErro; }
        public void setTitulo(String v)   { this.titulo = v; }
        public void setDescricao(String v){ this.descricao = v; }
        public void setTipoErro(String v) { this.tipoErro = v; }
    }

    public static class SolucaoRequest {
        private String solucao, script;
        public String getSolucao()       { return solucao; }
        public String getScript()        { return script; }
        public void setSolucao(String v) { this.solucao = v; }
        public void setScript(String v)  { this.script = v; }
    }

    public static class AnexoResponse {
        private Long id;
        private String nomeOriginal, contentType, url, tipo;
        public Long getId()              { return id; }
        public String getNomeOriginal()  { return nomeOriginal; }
        public String getContentType()   { return contentType; }
        public String getUrl()           { return url; }
        public String getTipo()          { return tipo; }
        public void setId(Long v)              { this.id = v; }
        public void setNomeOriginal(String v)  { this.nomeOriginal = v; }
        public void setContentType(String v)   { this.contentType = v; }
        public void setUrl(String v)           { this.url = v; }
        public void setTipo(String v)          { this.tipo = v; }
    }

    public static class ErroResponse {
        private Long id, criadorId;
        private String titulo, descricao, solucao, script, status, criadorNome, tipoErro;
        private LocalDateTime dataCriacao;
        private List<AnexoResponse> anexosBug, anexosSolucao;

        public Long getId()                        { return id; }
        public Long getCriadorId()                 { return criadorId; }
        public String getTitulo()                  { return titulo; }
        public String getDescricao()               { return descricao; }
        public String getSolucao()                 { return solucao; }
        public String getScript()                  { return script; }
        public String getStatus()                  { return status; }
        public String getCriadorNome()             { return criadorNome; }
        public String getTipoErro()                { return tipoErro; }
        public LocalDateTime getDataCriacao()      { return dataCriacao; }
        public List<AnexoResponse> getAnexosBug()     { return anexosBug; }
        public List<AnexoResponse> getAnexosSolucao() { return anexosSolucao; }
        public void setId(Long v)                       { this.id = v; }
        public void setCriadorId(Long v)                { this.criadorId = v; }
        public void setTitulo(String v)                 { this.titulo = v; }
        public void setDescricao(String v)              { this.descricao = v; }
        public void setSolucao(String v)                { this.solucao = v; }
        public void setScript(String v)                 { this.script = v; }
        public void setStatus(String v)                 { this.status = v; }
        public void setCriadorNome(String v)            { this.criadorNome = v; }
        public void setTipoErro(String v)               { this.tipoErro = v; }
        public void setDataCriacao(LocalDateTime v)     { this.dataCriacao = v; }
        public void setAnexosBug(List<AnexoResponse> v)     { this.anexosBug = v; }
        public void setAnexosSolucao(List<AnexoResponse> v) { this.anexosSolucao = v; }
    }

    // ─── Plantão ──────────────────────────────────────────────

    public static class PlantaoRequest {
        @NotNull  private com.webbeaga.sistema.entity.Plantao.TipoPlantao tipo;
        @NotNull  private LocalDate dataInicio;
        @NotNull  private LocalDate dataFim;
        @NotNull  private Long funcionarioId;
        private String observacao;
        public com.webbeaga.sistema.entity.Plantao.TipoPlantao getTipo() { return tipo; }
        public LocalDate getDataInicio()    { return dataInicio; }
        public LocalDate getDataFim()       { return dataFim; }
        public Long getFuncionarioId()      { return funcionarioId; }
        public String getObservacao()       { return observacao; }
        public void setTipo(com.webbeaga.sistema.entity.Plantao.TipoPlantao v) { this.tipo = v; }
        public void setDataInicio(LocalDate v)    { this.dataInicio = v; }
        public void setDataFim(LocalDate v)       { this.dataFim = v; }
        public void setFuncionarioId(Long v)      { this.funcionarioId = v; }
        public void setObservacao(String v)       { this.observacao = v; }
    }

    public static class PlantaoResponse {
        private Long id, funcionarioId;
        private String tipo, tipoDescricao, funcionarioNome, funcionarioCargo, criadoPorNome, observacao;
        private LocalDate dataInicio, dataFim;
        private LocalDateTime criadoEm;
        public Long getId()                  { return id; }
        public Long getFuncionarioId()       { return funcionarioId; }
        public String getTipo()              { return tipo; }
        public String getTipoDescricao()     { return tipoDescricao; }
        public String getFuncionarioNome()   { return funcionarioNome; }
        public String getFuncionarioCargo()  { return funcionarioCargo; }
        public String getCriadoPorNome()     { return criadoPorNome; }
        public String getObservacao()        { return observacao; }
        public LocalDate getDataInicio()     { return dataInicio; }
        public LocalDate getDataFim()        { return dataFim; }
        public LocalDateTime getCriadoEm()   { return criadoEm; }
        public void setId(Long v)                  { this.id = v; }
        public void setFuncionarioId(Long v)       { this.funcionarioId = v; }
        public void setTipo(String v)              { this.tipo = v; }
        public void setTipoDescricao(String v)     { this.tipoDescricao = v; }
        public void setFuncionarioNome(String v)   { this.funcionarioNome = v; }
        public void setFuncionarioCargo(String v)  { this.funcionarioCargo = v; }
        public void setCriadoPorNome(String v)     { this.criadoPorNome = v; }
        public void setObservacao(String v)        { this.observacao = v; }
        public void setDataInicio(LocalDate v)     { this.dataInicio = v; }
        public void setDataFim(LocalDate v)        { this.dataFim = v; }
        public void setCriadoEm(LocalDateTime v)   { this.criadoEm = v; }
    }

    // ─── Chamados ─────────────────────────────────────────────

    public static class ChamadoRequest {
        @NotBlank private String tipo;
        private String descricao;
        private Long postoId;
        public String getTipo()      { return tipo; }
        public String getDescricao() { return descricao; }
        public Long getPostoId()     { return postoId; }
        public void setTipo(String v)      { this.tipo = v; }
        public void setDescricao(String v) { this.descricao = v; }
        public void setPostoId(Long v)     { this.postoId = v; }
    }

    public static class ChamadoResponse {
        private Long id;
        private String tipo, tipoDescricao, descricao, status;
        private String usuarioNome, postoNome;
        private Long usuarioId, postoId;
        private LocalDateTime dataInicio, dataFim;
        private Long duracaoSegundos;
        public Long getId()                    { return id; }
        public String getTipo()                { return tipo; }
        public String getTipoDescricao()       { return tipoDescricao; }
        public String getDescricao()           { return descricao; }
        public String getStatus()              { return status; }
        public String getUsuarioNome()         { return usuarioNome; }
        public Long getUsuarioId()             { return usuarioId; }
        public String getPostoNome()           { return postoNome; }
        public Long getPostoId()               { return postoId; }
        public LocalDateTime getDataInicio()   { return dataInicio; }
        public LocalDateTime getDataFim()      { return dataFim; }
        public Long getDuracaoSegundos()       { return duracaoSegundos; }
        public void setId(Long v)                    { this.id = v; }
        public void setTipo(String v)                { this.tipo = v; }
        public void setTipoDescricao(String v)       { this.tipoDescricao = v; }
        public void setDescricao(String v)           { this.descricao = v; }
        public void setStatus(String v)              { this.status = v; }
        public void setUsuarioNome(String v)         { this.usuarioNome = v; }
        public void setUsuarioId(Long v)             { this.usuarioId = v; }
        public void setPostoNome(String v)           { this.postoNome = v; }
        public void setPostoId(Long v)               { this.postoId = v; }
        public void setDataInicio(LocalDateTime v)   { this.dataInicio = v; }
        public void setDataFim(LocalDateTime v)      { this.dataFim = v; }
        public void setDuracaoSegundos(Long v)       { this.duracaoSegundos = v; }
    }

    // ─── Ranking ──────────────────────────────────────────────

    public static class RankingItem {
        private Long usuarioId;
        private String usuarioNome;
        private int quantidade;
        public Long getUsuarioId()       { return usuarioId; }
        public String getUsuarioNome()   { return usuarioNome; }
        public int getQuantidade()       { return quantidade; }
        public void setUsuarioId(Long v)      { this.usuarioId = v; }
        public void setUsuarioNome(String v)  { this.usuarioNome = v; }
        public void setQuantidade(int v)      { this.quantidade = v; }
    }

    // ─── Avisos ───────────────────────────────────────────────

    public static class AvisoRequest {
        @NotBlank private String titulo;
        @NotBlank private String mensagem;
        public String getTitulo()   { return titulo; }
        public String getMensagem() { return mensagem; }
        public void setTitulo(String v)   { this.titulo = v; }
        public void setMensagem(String v) { this.mensagem = v; }
    }

    public static class AvisoResponse {
        private Long id;
        private String titulo, mensagem, criadoPorNome;
        private LocalDateTime criadoEm;
        public Long getId()               { return id; }
        public String getTitulo()         { return titulo; }
        public String getMensagem()       { return mensagem; }
        public String getCriadoPorNome()  { return criadoPorNome; }
        public LocalDateTime getCriadoEm(){ return criadoEm; }
        public void setId(Long v)                { this.id = v; }
        public void setTitulo(String v)          { this.titulo = v; }
        public void setMensagem(String v)        { this.mensagem = v; }
        public void setCriadoPorNome(String v)   { this.criadoPorNome = v; }
        public void setCriadoEm(LocalDateTime v) { this.criadoEm = v; }
    }

    // ─── Links Úteis ──────────────────────────────────────────

    public static class LinkRequest {
        @NotBlank private String titulo;
        @NotBlank private String url;
        public String getTitulo() { return titulo; }
        public String getUrl()    { return url; }
        public void setTitulo(String v) { this.titulo = v; }
        public void setUrl(String v)    { this.url = v; }
    }

    public static class LinkResponse {
        private Long id;
        private String titulo, url, criadoPorNome;
        private LocalDateTime criadoEm;
        public Long getId()              { return id; }
        public String getTitulo()        { return titulo; }
        public String getUrl()           { return url; }
        public String getCriadoPorNome() { return criadoPorNome; }
        public LocalDateTime getCriadoEm(){ return criadoEm; }
        public void setId(Long v)               { this.id = v; }
        public void setTitulo(String v)         { this.titulo = v; }
        public void setUrl(String v)            { this.url = v; }
        public void setCriadoPorNome(String v)  { this.criadoPorNome = v; }
        public void setCriadoEm(LocalDateTime v){ this.criadoEm = v; }
    }

    // ─── ApiResponse genérico ─────────────────────────────────

    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;
        public ApiResponse() {}
        public boolean isSuccess()  { return success; }
        public String getMessage()  { return message; }
        public T getData()          { return data; }
        public void setSuccess(boolean v) { this.success = v; }
        public void setMessage(String v)  { this.message = v; }
        public void setData(T v)          { this.data = v; }

        public static <T> ApiResponse<T> ok(T data) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = true; r.data = data; return r;
        }
        public static <T> ApiResponse<T> ok(String msg, T data) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = true; r.message = msg; r.data = data; return r;
        }
        public static <T> ApiResponse<T> error(String msg) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = false; r.message = msg; return r;
        }
    }
}
