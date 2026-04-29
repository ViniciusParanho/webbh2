package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "erros")
public class Erro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String solucao;

    @Column(columnDefinition = "TEXT")
    private String script;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusErro status = StatusErro.ABERTO;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id")
    private Usuario criador;

    public enum StatusErro { ABERTO, RESOLVIDO }
    public enum TipoErro { PDV, GERENCIAL, OUTROS }

    @Enumerated(EnumType.STRING)
    private TipoErro tipoErro = TipoErro.OUTROS;

    public TipoErro getTipoErro()         { return tipoErro; }
    public void setTipoErro(TipoErro v)   { this.tipoErro = v; }

    public Long getId()                    { return id; }
    public void setId(Long v)              { this.id = v; }
    public String getTitulo()              { return titulo; }
    public void setTitulo(String v)        { this.titulo = v; }
    public String getDescricao()           { return descricao; }
    public void setDescricao(String v)     { this.descricao = v; }
    public String getSolucao()             { return solucao; }
    public void setSolucao(String v)       { this.solucao = v; }
    public String getScript()              { return script; }
    public void setScript(String v)        { this.script = v; }
    public StatusErro getStatus()          { return status; }
    public void setStatus(StatusErro v)    { this.status = v; }
    public LocalDateTime getDataCriacao()  { return dataCriacao; }
    public void setDataCriacao(LocalDateTime v) { this.dataCriacao = v; }
    public Usuario getCriador()            { return criador; }
    public void setCriador(Usuario v)      { this.criador = v; }
}
