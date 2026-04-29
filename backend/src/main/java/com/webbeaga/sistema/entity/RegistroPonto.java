package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "registros_ponto",
       uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "data"}))
public class RegistroPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Column(name = "hora_saida_almoco")
    private LocalTime horaSaidaAlmoco;

    @Column(name = "hora_retorno_almoco")
    private LocalTime horaRetornoAlmoco;

    @Column(name = "hora_saida")
    private LocalTime horaSaida;

    @Column(name = "total_minutos")
    private Integer totalMinutos;

    @Column(name = "minutos_extras")
    private Integer minutosExtras;

    @Column(name = "minutos_devendo")
    private Integer minutosDevendo;

    @Enumerated(EnumType.STRING)
    private StatusPonto status = StatusPonto.PENDENTE;

    private String observacao;

    @Column(name = "latitude_entrada")
    private Double latitudeEntrada;

    @Column(name = "longitude_entrada")
    private Double longitudeEntrada;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public RegistroPonto() {}

    public void recalcular() {
        if (horaEntrada == null || horaSaida == null) return;
        int trabalhado = (int) java.time.Duration.between(horaEntrada, horaSaida).toMinutes();
        if (horaSaidaAlmoco != null && horaRetornoAlmoco != null) {
            trabalhado -= (int) java.time.Duration.between(horaSaidaAlmoco, horaRetornoAlmoco).toMinutes();
        }
        this.totalMinutos = Math.max(0, trabalhado);
        boolean sabado = data != null && data.getDayOfWeek() == DayOfWeek.SATURDAY;
        int meta = sabado ? 240
                 : (usuario != null && usuario.getCargaHorariaDiaria() != null)
                   ? usuario.getCargaHorariaDiaria() * 60 : 480;
        this.minutosExtras  = totalMinutos > meta ? totalMinutos - meta : 0;
        this.minutosDevendo = totalMinutos < meta ? meta - totalMinutos : 0;
        this.status = totalMinutos > meta       ? StatusPonto.HORA_EXTRA
                    : totalMinutos >= meta - 15  ? StatusPonto.NORMAL
                    : StatusPonto.PARCIAL;
    }

    public Long getId()                        { return id; }
    public Usuario getUsuario()                { return usuario; }
    public LocalDate getData()                 { return data; }
    public LocalTime getHoraEntrada()          { return horaEntrada; }
    public LocalTime getHoraSaidaAlmoco()      { return horaSaidaAlmoco; }
    public LocalTime getHoraRetornoAlmoco()    { return horaRetornoAlmoco; }
    public LocalTime getHoraSaida()            { return horaSaida; }
    public Integer getTotalMinutos()           { return totalMinutos; }
    public Integer getMinutosExtras()          { return minutosExtras; }
    public Integer getMinutosDevendo()         { return minutosDevendo; }
    public StatusPonto getStatus()             { return status; }
    public String getObservacao()              { return observacao; }
    public Double getLatitudeEntrada()         { return latitudeEntrada; }
    public Double getLongitudeEntrada()        { return longitudeEntrada; }

    public void setUsuario(Usuario v)          { this.usuario = v; }
    public void setData(LocalDate v)           { this.data = v; }
    public void setHoraEntrada(LocalTime v)    { this.horaEntrada = v; }
    public void setHoraSaidaAlmoco(LocalTime v){ this.horaSaidaAlmoco = v; }
    public void setHoraRetornoAlmoco(LocalTime v){ this.horaRetornoAlmoco = v; }
    public void setHoraSaida(LocalTime v)      { this.horaSaida = v; }
    public void setTotalMinutos(Integer v)     { this.totalMinutos = v; }
    public void setMinutosExtras(Integer v)    { this.minutosExtras = v; }
    public void setMinutosDevendo(Integer v)   { this.minutosDevendo = v; }
    public void setStatus(StatusPonto v)       { this.status = v; }
    public void setObservacao(String v)        { this.observacao = v; }
    public void setLatitudeEntrada(Double v)   { this.latitudeEntrada = v; }
    public void setLongitudeEntrada(Double v)  { this.longitudeEntrada = v; }

    public enum StatusPonto { PENDENTE, EM_CURSO, NORMAL, HORA_EXTRA, PARCIAL, AUSENCIA }
}
