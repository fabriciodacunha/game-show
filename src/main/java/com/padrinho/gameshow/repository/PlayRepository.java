package com.padrinho.gameshow.repository;

import com.padrinho.gameshow.domain.Play;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Play entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayRepository extends JpaRepository<Play, Long> {}
