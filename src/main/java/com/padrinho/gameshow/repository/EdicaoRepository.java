package com.padrinho.gameshow.repository;

import com.padrinho.gameshow.domain.Edicao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Edicao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EdicaoRepository extends JpaRepository<Edicao, Long> {}
