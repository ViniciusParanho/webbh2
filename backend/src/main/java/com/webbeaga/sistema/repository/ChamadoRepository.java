package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    @Query("SELECT c FROM Chamado c WHERE c.usuario.id = :uid ORDER BY c.dataInicio DESC")
    List<Chamado> findByUsuarioId(@Param("uid") Long uid);

    List<Chamado> findAllByOrderByDataInicioDesc();

    @Query("SELECT c.usuario.id FROM Chamado c WHERE c.status = 'ABERTO'")
    List<Long> findUsuarioIdsEmAtendimento();

    @Query("SELECT c.usuario.id, MAX(c.dataInicio) FROM Chamado c WHERE c.usuario.id IN :uids AND c.dataInicio >= :inicioDia GROUP BY c.usuario.id")
    List<Object[]> findUltimoInicioChamadoHoje(@Param("uids") List<Long> uids, @Param("inicioDia") java.time.LocalDateTime inicioDia);

    @Modifying
    @Query("UPDATE Chamado c SET c.posto = null WHERE c.posto.id IN :postoIds")
    void desvincularPostos(@Param("postoIds") List<Long> postoIds);
}
