package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "redes")
public class Rede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String bandeira;
    private String anyDeskSenha;
    private Boolean ativa = true;

    @OneToMany(mappedBy = "rede", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Posto> postos = new ArrayList<>();

    public Rede() {}

    public Long getId()           { return id; }
    public String getNome()       { return nome; }
    public String getBandeira()      { return bandeira; }
    public String getAnyDeskSenha()  { return anyDeskSenha; }
    public Boolean getAtiva()        { return ativa; }
    public List<Posto> getPostos()   { return postos; }

    public void setNome(String v)          { this.nome = v; }
    public void setBandeira(String v)      { this.bandeira = v; }
    public void setAnyDeskSenha(String v)  { this.anyDeskSenha = v; }
    public void setAtiva(Boolean v)        { this.ativa = v; }
}
