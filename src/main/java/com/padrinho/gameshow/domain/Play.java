package com.padrinho.gameshow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Play.
 */
@Entity
@Table(name = "play")
public class Play implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "play_data")
    private ZonedDateTime playData;

    @OneToMany(mappedBy = "play")
    @JsonIgnoreProperties(value = { "questao", "participante", "play" }, allowSetters = true)
    private Set<Resposta> respostas = new HashSet<>();

    @OneToMany(mappedBy = "play")
    @JsonIgnoreProperties(value = { "respostas", "play" }, allowSetters = true)
    private Set<Participante> participantes = new HashSet<>();

    @JsonIgnoreProperties(value = { "play", "questaos" }, allowSetters = true)
    @OneToOne(mappedBy = "play")
    private Edicao edicao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Play id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getPlayData() {
        return this.playData;
    }

    public Play playData(ZonedDateTime playData) {
        this.setPlayData(playData);
        return this;
    }

    public void setPlayData(ZonedDateTime playData) {
        this.playData = playData;
    }

    public Set<Resposta> getRespostas() {
        return this.respostas;
    }

    public void setRespostas(Set<Resposta> respostas) {
        if (this.respostas != null) {
            this.respostas.forEach(i -> i.setPlay(null));
        }
        if (respostas != null) {
            respostas.forEach(i -> i.setPlay(this));
        }
        this.respostas = respostas;
    }

    public Play respostas(Set<Resposta> respostas) {
        this.setRespostas(respostas);
        return this;
    }

    public Play addResposta(Resposta resposta) {
        this.respostas.add(resposta);
        resposta.setPlay(this);
        return this;
    }

    public Play removeResposta(Resposta resposta) {
        this.respostas.remove(resposta);
        resposta.setPlay(null);
        return this;
    }

    public Set<Participante> getParticipantes() {
        return this.participantes;
    }

    public void setParticipantes(Set<Participante> participantes) {
        if (this.participantes != null) {
            this.participantes.forEach(i -> i.setPlay(null));
        }
        if (participantes != null) {
            participantes.forEach(i -> i.setPlay(this));
        }
        this.participantes = participantes;
    }

    public Play participantes(Set<Participante> participantes) {
        this.setParticipantes(participantes);
        return this;
    }

    public Play addParticipante(Participante participante) {
        this.participantes.add(participante);
        participante.setPlay(this);
        return this;
    }

    public Play removeParticipante(Participante participante) {
        this.participantes.remove(participante);
        participante.setPlay(null);
        return this;
    }

    public Edicao getEdicao() {
        return this.edicao;
    }

    public void setEdicao(Edicao edicao) {
        if (this.edicao != null) {
            this.edicao.setPlay(null);
        }
        if (edicao != null) {
            edicao.setPlay(this);
        }
        this.edicao = edicao;
    }

    public Play edicao(Edicao edicao) {
        this.setEdicao(edicao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Play)) {
            return false;
        }
        return id != null && id.equals(((Play) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Play{" +
            "id=" + getId() +
            ", playData='" + getPlayData() + "'" +
            "}";
    }
}
