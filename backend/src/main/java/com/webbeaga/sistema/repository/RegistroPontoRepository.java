package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {

    Optional<RegistroPonto> findByUsuarioIdAndData(Long usuarioId, LocalDate data);

    @Query("SELECT r FROM RegistroPonto r WHERE r.usuario.id = :uid " +
           "AND YEAR(r.data) = :ano AND MONTH(r.data) = :mes ORDER BY r.data")
    List<RegistroPonto> findByUsuarioIdAndMes(
        @Param("uid") Long usuarioId,
        @Param("ano") int ano,
        @Param("mes") int mes);

    List<RegistroPonto> findByUsuarioIdAndDataBetweenOrderByData(
        Long usuarioId, LocalDate inicio, LocalDate fim);

    @Query("SELECT r FROM RegistroPonto r WHERE r.data = :hoje")
    List<RegistroPonto> findByData(@Param("hoje") LocalDate hoje);
}
