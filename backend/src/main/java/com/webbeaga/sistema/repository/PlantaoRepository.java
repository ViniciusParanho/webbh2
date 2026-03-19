package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Plantao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface PlantaoRepository extends JpaRepository<Plantao, Long> {
    List<Plantao> findAllByOrderByDataInicioAsc();

    @Query("SELECT p FROM Plantao p WHERE p.dataInicio <= :fim AND p.dataFim >= :inicio ORDER BY p.dataInicio ASC")
    List<Plantao> findPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
