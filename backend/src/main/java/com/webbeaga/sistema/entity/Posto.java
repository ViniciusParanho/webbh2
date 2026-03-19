package com.webbeaga.sistema.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "postos")
public class Posto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String endereco;
    private String cidade;
    private String anyDeskId;
    private String anyDeskSenha;

    @Column(name = "sistema_instalado")
    private String sistemaInstalado = "QualityPos";

    private String observacoes;
    private Boolean online = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rede_id")
    private Rede rede;

    public Posto() {}

    public Long getId()                 { return id; }
    public String getNome()             { return nome; }
    public String getEndereco()         { return endereco; }
    public String getCidade()           { return cidade; }
    public String getAnyDeskId()        { return anyDeskId; }
    public String getAnyDeskSenha()     { return anyDeskSenha; }
    public String getSistemaInstalado() { return sistemaInstalado; }
    public String getObservacoes()      { return observacoes; }
    public Boolean getOnline()          { return online; }
    public Rede getRede()               { return rede; }

    public void setNome(String v)             { this.nome = v; }
    public void setEndereco(String v)         { this.endereco = v; }
    public void setCidade(String v)           { this.cidade = v; }
    public void setAnyDeskId(String v)        { this.anyDeskId = v; }
    public void setAnyDeskSenha(String v)     { this.anyDeskSenha = v; }
    public void setSistemaInstalado(String v) { this.sistemaInstalado = v; }
    public void setObservacoes(String v)      { this.observacoes = v; }
    public void setOnline(Boolean v)          { this.online = v; }
    public void setRede(Rede v)               { this.rede = v; }
}
