package com.webbeaga.sistema.service;

import com.webbeaga.sistema.dto.DTOs.*;
import com.webbeaga.sistema.entity.Plantao;
import com.webbeaga.sistema.entity.Usuario;
import com.webbeaga.sistema.repository.PlantaoRepository;
import com.webbeaga.sistema.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantaoService {

    private final PlantaoRepository plantaoRepo;
    private final UsuarioRepository usuarioRepo;

    public PlantaoService(PlantaoRepository plantaoRepo, UsuarioRepository usuarioRepo) {
        this.plantaoRepo = plantaoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<PlantaoResponse> listarTodos() {
        return plantaoRepo.findAllByOrderByDataInicioAsc()
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlantaoResponse> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return plantaoRepo.findPorPeriodo(inicio, fim)
            .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public PlantaoResponse criar(PlantaoRequest req, Usuario criadoPor) {
        if (req.getDataInicio().isAfter(req.getDataFim()))
            throw new IllegalArgumentException("Data início não pode ser após data fim");

        Usuario func = usuarioRepo.findById(req.getFuncionarioId())
            .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado"));

        Plantao p = new Plantao();
        p.setTipo(req.getTipo());
        p.setDataInicio(req.getDataInicio());
        p.setDataFim(req.getDataFim());
        p.setFuncionario(func);
        p.setCriadoPor(criadoPor);
        p.setObservacao(req.getObservacao());
        return toResponse(plantaoRepo.save(p));
    }

    @Transactional
    public void excluir(Long id) {
        if (!plantaoRepo.existsById(id))
            throw new IllegalArgumentException("Plantão não encontrado");
        plantaoRepo.deleteById(id);
    }

    private PlantaoResponse toResponse(Plantao p) {
        PlantaoResponse r = new PlantaoResponse();
        r.setId(p.getId());
        r.setTipo(p.getTipo().name());
        r.setTipoDescricao(p.getTipo().getDescricao());
        r.setDataInicio(p.getDataInicio());
        r.setDataFim(p.getDataFim());
        r.setObservacao(p.getObservacao());
        r.setCriadoEm(p.getCriadoEm());
        if (p.getFuncionario() != null) {
            r.setFuncionarioId(p.getFuncionario().getId());
            r.setFuncionarioNome(p.getFuncionario().getNomeCompleto());
            r.setFuncionarioCargo(p.getFuncionario().getCargo());
        }
        if (p.getCriadoPor() != null) {
            r.setCriadoPorNome(p.getCriadoPor().getNomeCompleto());
        }
        return r;
    }
}
