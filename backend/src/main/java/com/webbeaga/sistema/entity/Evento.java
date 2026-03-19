package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipo;

    @Column(nullable = false)
    private String rede;

    @Column(nullable = false)
    private String posto;

    @Column(name = "data_evento", nullable = false)
    private LocalDate dataEvento;

    @Column(name = "hora_evento")
    private LocalTime horaEvento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "evento_responsaveis",
               joinColumns = @JoinColumn(name = "evento_id"),
               inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private List<Usuario> responsaveis = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusEvento status = StatusEvento.AGENDADO;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Evento() {}

    public Long getId()                { return id; }
    public TipoEvento getTipo()            { return tipo; }
    public String getRede()                { return rede; }
    public String getPosto()               { return posto; }
    public LocalDate getDataEvento()       { return dataEvento; }
    public LocalTime getHoraEvento()       { return horaEvento; }
    public List<Usuario> getResponsaveis() { return responsaveis; }
    public Usuario getCriadoPor()          { return criadoPor; }
    public String getObservacao()          { return observacao; }
    public StatusEvento getStatus()        { return status; }
    public LocalDateTime getCriadoEm()     { return criadoEm; }

    public void setTipo(TipoEvento v)            { this.tipo = v; }
    public void setRede(String v)                { this.rede = v; }
    public void setPosto(String v)               { this.posto = v; }
    public void setDataEvento(LocalDate v)       { this.dataEvento = v; }
    public void setHoraEvento(LocalTime v)       { this.horaEvento = v; }
    public void setResponsaveis(List<Usuario> v) { this.responsaveis = v; }
    public void setCriadoPor(Usuario v)          { this.criadoPor = v; }
    public void setObservacao(String v)          { this.observacao = v; }
    public void setStatus(StatusEvento v)        { this.status = v; }

    public enum TipoEvento {
        IMPLANTACAO("Implantação"),
        INSTALACAO_PDV("Instalação de Sistema PDV"),
        CO_WORK("Co-work"),
        TREINAMENTO("Treinamento");

        private final String descricao;
        TipoEvento(String d) { this.descricao = d; }
        public String getDescricao() { return descricao; }
    }

    public enum StatusEvento { AGENDADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO }
}
