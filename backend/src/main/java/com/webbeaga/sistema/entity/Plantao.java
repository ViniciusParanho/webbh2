package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plantoes")
public class Plantao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPlantao tipo;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Usuario funcionario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    private String observacao;

    private LocalDateTime criadoEm = LocalDateTime.now();

    public enum TipoPlantao {
        SEMANA("Plantão Semana"),
        FIM_SEMANA("Plantão Fim de Semana");

        private final String descricao;
        TipoPlantao(String d) { this.descricao = d; }
        public String getDescricao() { return descricao; }
    }

    public Long getId()                      { return id; }
    public void setId(Long v)                { this.id = v; }
    public TipoPlantao getTipo()             { return tipo; }
    public void setTipo(TipoPlantao v)       { this.tipo = v; }
    public LocalDate getDataInicio()         { return dataInicio; }
    public void setDataInicio(LocalDate v)   { this.dataInicio = v; }
    public LocalDate getDataFim()            { return dataFim; }
    public void setDataFim(LocalDate v)      { this.dataFim = v; }
    public Usuario getFuncionario()          { return funcionario; }
    public void setFuncionario(Usuario v)    { this.funcionario = v; }
    public Usuario getCriadoPor()            { return criadoPor; }
    public void setCriadoPor(Usuario v)      { this.criadoPor = v; }
    public String getObservacao()            { return observacao; }
    public void setObservacao(String v)      { this.observacao = v; }
    public LocalDateTime getCriadoEm()       { return criadoEm; }
    public void setCriadoEm(LocalDateTime v) { this.criadoEm = v; }
}
