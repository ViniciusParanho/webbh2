package com.webbeaga.sistema.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "erro_anexos")
public class ErroAnexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "erro_id", nullable = false)
    private Erro erro;

    private String nomeArquivo;
    private String nomeOriginal;
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnexo tipo;

    public enum TipoAnexo { BUG, SOLUCAO }

    public Long getId()                  { return id; }
    public void setId(Long v)            { this.id = v; }
    public Erro getErro()                { return erro; }
    public void setErro(Erro v)          { this.erro = v; }
    public String getNomeArquivo()       { return nomeArquivo; }
    public void setNomeArquivo(String v) { this.nomeArquivo = v; }
    public String getNomeOriginal()      { return nomeOriginal; }
    public void setNomeOriginal(String v){ this.nomeOriginal = v; }
    public String getContentType()       { return contentType; }
    public void setContentType(String v) { this.contentType = v; }
    public TipoAnexo getTipo()           { return tipo; }
    public void setTipo(TipoAnexo v)     { this.tipo = v; }
}
