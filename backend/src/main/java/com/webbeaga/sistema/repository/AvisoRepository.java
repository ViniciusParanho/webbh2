package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    List<Aviso> findAllByOrderByCriadoEmDesc();
}
