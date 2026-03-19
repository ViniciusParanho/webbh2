package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Erro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ErroRepository extends JpaRepository<Erro, Long> {
    List<Erro> findAllByOrderByDataCriacaoDesc();
}
