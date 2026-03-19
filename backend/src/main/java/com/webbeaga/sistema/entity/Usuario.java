package com.webbeaga.sistema.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(nullable = false)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String email;
    private String telefone;

    @Column(name = "carga_horaria_diaria")
    private Integer cargaHorariaDiaria = 8;

    private Boolean ativo = true;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    public Usuario() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return Boolean.TRUE.equals(ativo); }

    public Long getId()                    { return id; }
    public String getNomeCompleto()        { return nomeCompleto; }
    public String getCargo()               { return cargo; }
    public Role getRole()                  { return role; }
    public String getEmail()               { return email; }
    public String getTelefone()            { return telefone; }
    public Integer getCargaHorariaDiaria() { return cargaHorariaDiaria; }
    public Boolean getAtivo()              { return ativo; }
    public LocalDateTime getCriadoEm()     { return criadoEm; }

    public void setUsername(String v)            { this.username = v; }
    public void setPassword(String v)            { this.password = v; }
    public void setNomeCompleto(String v)        { this.nomeCompleto = v; }
    public void setCargo(String v)               { this.cargo = v; }
    public void setRole(Role v)                  { this.role = v; }
    public void setEmail(String v)               { this.email = v; }
    public void setTelefone(String v)            { this.telefone = v; }
    public void setCargaHorariaDiaria(Integer v) { this.cargaHorariaDiaria = v; }
    public void setAtivo(Boolean v)              { this.ativo = v; }

    public enum Role { ADMIN, USER }
}
