package com.padrinho.gameshow.service.impl;

import com.padrinho.gameshow.domain.Play;
import com.padrinho.gameshow.repository.PlayRepository;
import com.padrinho.gameshow.service.PlayService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Play}.
 */
@Service
@Transactional
public class PlayServiceImpl implements PlayService {

    private final Logger log = LoggerFactory.getLogger(PlayServiceImpl.class);

    private final PlayRepository playRepository;

    public PlayServiceImpl(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    @Override
    public Play save(Play play) {
        log.debug("Request to save Play : {}", play);
        return playRepository.save(play);
    }

    @Override
    public Optional<Play> partialUpdate(Play play) {
        log.debug("Request to partially update Play : {}", play);

        return playRepository
            .findById(play.getId())
            .map(existingPlay -> {
                if (play.getPlayData() != null) {
                    existingPlay.setPlayData(play.getPlayData());
                }

                return existingPlay;
            })
            .map(playRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Play> findAll(Pageable pageable) {
        log.debug("Request to get all Plays");
        return playRepository.findAll(pageable);
    }

    /**
     *  Get all the plays where Edicao is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Play> findAllWhereEdicaoIsNull() {
        log.debug("Request to get all plays where Edicao is null");
        return StreamSupport
            .stream(playRepository.findAll().spliterator(), false)
            .filter(play -> play.getEdicao() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Play> findOne(Long id) {
        log.debug("Request to get Play : {}", id);
        return playRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Play : {}", id);
        playRepository.deleteById(id);
    }
}
