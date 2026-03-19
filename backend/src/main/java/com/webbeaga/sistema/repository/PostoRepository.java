package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Posto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostoRepository extends JpaRepository<Posto, Long> {
    List<Posto> findByRedeId(Long redeId);
}
