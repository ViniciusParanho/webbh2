package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.RegistroPonto;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.RegistroPontoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final RegistroPontoRepository pontoRepo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository ur, RegistroPontoRepository pr, PasswordEncoder enc) {
        this.usuarioRepo = ur;
        this.pontoRepo   = pr;
        this.encoder     = enc;
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepo.findByAtivoTrue().stream()
            .map(this::toResponse).collect(Collectors.toList());
    }

    public UsuarioResponse getById(Long id) {
        return usuarioRepo.findById(id)
            .map(this::toResponse)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
    }

    @Transactional
    public UsuarioResponse criar(UsuarioRequest req) {
        if (usuarioRepo.findByUsername(req.getUsername()).isPresent())
            throw new IllegalArgumentException("Username ja em uso: " + req.getUsername());

        Usuario u = new Usuario();
        u.setUsername(req.getUsername());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setNomeCompleto(req.getNomeCompleto());
        u.setCargo(req.getCargo());
        u.setRole("ADMIN".equalsIgnoreCase(req.getRole()) ? Usuario.Role.ADMIN : Usuario.Role.USER);
        u.setEmail(req.getEmail());
        u.setTelefone(req.getTelefone());
        if (req.getCargaHorariaDiaria() != null) u.setCargaHorariaDiaria(req.getCargaHorariaDiaria());
        return toResponse(usuarioRepo.save(u));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest req) {
        Usuario u = usuarioRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
        if (req.getNomeCompleto() != null) u.setNomeCompleto(req.getNomeCompleto());
        if (req.getCargo()        != null) u.setCargo(req.getCargo());
        if (req.getEmail()        != null) u.setEmail(req.getEmail());
        if (req.getTelefone()     != null) u.setTelefone(req.getTelefone());
        if (req.getPassword()     != null && !req.getPassword().isBlank())
            u.setPassword(encoder.encode(req.getPassword()));
        if (req.getCargaHorariaDiaria() != null) u.setCargaHorariaDiaria(req.getCargaHorariaDiaria());
        return toResponse(usuarioRepo.save(u));
    }

    @Transactional
    public void desativar(Long id) {
        Usuario u = usuarioRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado: " + id));
        u.setAtivo(false);
        usuarioRepo.save(u);
    }

    private UsuarioResponse toResponse(Usuario u) {
        UsuarioResponse r = new UsuarioResponse();
        r.setId(u.getId());
        r.setUsername(u.getUsername());
        r.setNomeCompleto(u.getNomeCompleto());
        r.setCargo(u.getCargo());
        r.setRole(u.getRole().name());
        r.setEmail(u.getEmail());
        r.setTelefone(u.getTelefone());
        r.setCargaHorariaDiaria(u.getCargaHorariaDiaria());
        r.setAtivo(u.getAtivo());
        r.setCriadoEm(u.getCriadoEm());

        // Status ponto hoje
        Optional<RegistroPonto> pontoHoje = pontoRepo.findByUsuarioIdAndData(u.getId(), LocalDate.now());
        if (pontoHoje.isPresent()) {
            RegistroPonto p = pontoHoje.get();
            r.setStatusPontoHoje(p.getStatus() != null ? p.getStatus().name() : "PENDENTE");
        } else {
            r.setStatusPontoHoje("AUSENTE");
        }

        // Totais do mês atual
        LocalDate hoje = LocalDate.now();
        List<RegistroPonto> registros = pontoRepo.findByUsuarioIdAndMes(u.getId(), hoje.getYear(), hoje.getMonthValue());
        int totalMin = registros.stream()
            .filter(p -> p.getTotalMinutos() != null)
            .mapToInt(RegistroPonto::getTotalMinutos).sum();
        int extrasMin = registros.stream()
            .filter(p -> p.getMinutosExtras() != null)
            .mapToInt(RegistroPonto::getMinutosExtras).sum();
        long dias = registros.stream().filter(p -> p.getHoraSaida() != null).count();
        int diasUteis = calcDiasUteis(hoje.getYear(), hoje.getMonthValue());
        r.setHorasMes(totalMin / 60);
        r.setMinutosExtrasMes(extrasMin);
        r.setAusenciasMes((int) Math.max(0, diasUteis - dias));
        return r;
    }

    private int calcDiasUteis(int ano, int mes) {
        LocalDate d   = LocalDate.of(ano, mes, 1);
        LocalDate fim = d.withDayOfMonth(d.lengthOfMonth());
        LocalDate hoje = LocalDate.now();
        if (fim.isAfter(hoje)) fim = hoje;
        int count = 0;
        while (!d.isAfter(fim)) { if (d.getDayOfWeek().getValue() <= 5) count++; d = d.plusDays(1); }
        return count;
    }
}
