package com.anderson_monte.desafio_tecnico_sgta.repositories;

import com.anderson_monte.desafio_tecnico_sgta.models.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<List, Long>, JpaSpecificationExecutor<List> {
    Boolean existsByName(String name);
    Optional<List> findByName(String name);
}
