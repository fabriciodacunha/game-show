package com.padrinho.gameshow.repository;

import com.padrinho.gameshow.domain.Participante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Participante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {}
