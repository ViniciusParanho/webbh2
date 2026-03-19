package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findAllByOrderByDataEventoAscHoraEventoAsc();

    @Query("SELECT e FROM Evento e WHERE e.dataEvento >= :hoje " +
           "AND e.status = 'AGENDADO' ORDER BY e.dataEvento ASC, e.horaEvento ASC")
    List<Evento> findProximos(@Param("hoje") LocalDate hoje);

    List<Evento> findByDataEventoBetweenOrderByDataEventoAscHoraEventoAsc(
        LocalDate inicio, LocalDate fim);

    long countByStatus(Evento.StatusEvento status);
}
