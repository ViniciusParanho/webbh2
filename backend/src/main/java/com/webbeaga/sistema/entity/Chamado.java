package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chamados")
public class Chamado {

    public enum TipoChamado {
        PDV("PDV"),
        RETAGUARDA("Retaguarda"),
        INSTALACAO_PDV("Instalação PDV"),
        MAQUININHA("Maquininha"),
        SPED("Sped"),
        IMPRESSORA("Impressora"),
        CERTIFICADO("Certificado");

        private final String descricao;
        TipoChamado(String d) { this.descricao = d; }
        public String getDescricao() { return descricao; }
    }

    public enum StatusChamado { ABERTO, CONCLUIDO }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoChamado tipo;

    @Column(length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posto_id")
    private Posto posto;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    /** Duração em segundos, preenchida ao concluir */
    private Long duracaoSegundos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChamado status = StatusChamado.ABERTO;

    public Long getId()                    { return id; }
    public TipoChamado getTipo()           { return tipo; }
    public String getDescricao()           { return descricao; }
    public Usuario getUsuario()            { return usuario; }
    public Posto getPosto()                { return posto; }
    public LocalDateTime getDataInicio()   { return dataInicio; }
    public LocalDateTime getDataFim()      { return dataFim; }
    public Long getDuracaoSegundos()       { return duracaoSegundos; }
    public StatusChamado getStatus()       { return status; }

    public void setId(Long v)                    { this.id = v; }
    public void setTipo(TipoChamado v)           { this.tipo = v; }
    public void setDescricao(String v)           { this.descricao = v; }
    public void setUsuario(Usuario v)            { this.usuario = v; }
    public void setPosto(Posto v)                { this.posto = v; }
    public void setDataInicio(LocalDateTime v)   { this.dataInicio = v; }
    public void setDataFim(LocalDateTime v)      { this.dataFim = v; }
    public void setDuracaoSegundos(Long v)       { this.duracaoSegundos = v; }
    public void setStatus(StatusChamado v)       { this.status = v; }
}
