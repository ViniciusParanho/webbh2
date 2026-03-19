package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Rede;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RedeRepository extends JpaRepository<Rede, Long> {
    List<Rede> findByAtivaTrue();
}
