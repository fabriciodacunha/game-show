package com.padrinho.gameshow.repository;

import com.padrinho.gameshow.domain.Questao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Questao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {}
