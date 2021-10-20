package com.padrinho.gameshow.service.impl;

import com.padrinho.gameshow.domain.Participante;
import com.padrinho.gameshow.repository.ParticipanteRepository;
import com.padrinho.gameshow.service.ParticipanteService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Participante}.
 */
@Service
@Transactional
public class ParticipanteServiceImpl implements ParticipanteService {

    private final Logger log = LoggerFactory.getLogger(ParticipanteServiceImpl.class);

    private final ParticipanteRepository participanteRepository;

    public ParticipanteServiceImpl(ParticipanteRepository participanteRepository) {
        this.participanteRepository = participanteRepository;
    }

    @Override
    public Participante save(Participante participante) {
        log.debug("Request to save Participante : {}", participante);
        return participanteRepository.save(participante);
    }

    @Override
    public Optional<Participante> partialUpdate(Participante participante) {
        log.debug("Request to partially update Participante : {}", participante);

        return participanteRepository
            .findById(participante.getId())
            .map(existingParticipante -> {
                if (participante.getCarastroUsuario() != null) {
                    existingParticipante.setCarastroUsuario(participante.getCarastroUsuario());
                }
                if (participante.getParticipanteNome() != null) {
                    existingParticipante.setParticipanteNome(participante.getParticipanteNome());
                }
                if (participante.getParticipanteEmail() != null) {
                    existingParticipante.setParticipanteEmail(participante.getParticipanteEmail());
                }
                if (participante.getParticipanteDataDeNascimento() != null) {
                    existingParticipante.setParticipanteDataDeNascimento(participante.getParticipanteDataDeNascimento());
                }

                return existingParticipante;
            })
            .map(participanteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Participante> findAll() {
        log.debug("Request to get all Participantes");
        return participanteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Participante> findOne(Long id) {
        log.debug("Request to get Participante : {}", id);
        return participanteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participante : {}", id);
        participanteRepository.deleteById(id);
    }
}
