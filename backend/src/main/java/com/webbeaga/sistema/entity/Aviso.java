package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avisos")
public class Aviso {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por_id")
    private Usuario criadoPor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Long getId()                { return id; }
    public String getTitulo()          { return titulo; }
    public String getMensagem()        { return mensagem; }
    public Usuario getCriadoPor()      { return criadoPor; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setId(Long v)               { this.id = v; }
    public void setTitulo(String v)         { this.titulo = v; }
    public void setMensagem(String v)       { this.mensagem = v; }
    public void setCriadoPor(Usuario v)     { this.criadoPor = v; }
    public void setCriadoEm(LocalDateTime v){ this.criadoEm = v; }
}
