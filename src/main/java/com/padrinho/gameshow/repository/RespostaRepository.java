package com.padrinho.gameshow.repository;

import com.padrinho.gameshow.domain.Resposta;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Resposta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {}
