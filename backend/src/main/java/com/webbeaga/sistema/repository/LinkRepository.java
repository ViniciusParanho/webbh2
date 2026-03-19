package com.webbeaga.sistema.repository;

import com.webbeaga.sistema.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findAllByOrderByCriadoEmAsc();
}
