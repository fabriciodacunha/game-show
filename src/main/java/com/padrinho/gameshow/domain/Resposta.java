package com.padrinho.gameshow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.padrinho.gameshow.domain.enumeration.Alternativa;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Resposta.
 */
@Entity
@Table(name = "resposta")
public class Resposta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "resposta_data")
    private ZonedDateTime respostaData;

    @Enumerated(EnumType.STRING)
    @Column(name = "resposta_alternativa")
    private Alternativa respostaAlternativa;

    @Column(name = "resposta_certa")
    private Boolean respostaCerta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "respostas", "edicao" }, allowSetters = true)
    private Questao questao;

    @ManyToOne
    @JsonIgnoreProperties(value = { "respostas", "play" }, allowSetters = true)
    private Participante participante;

    @ManyToOne
    @JsonIgnoreProperties(value = { "respostas", "participantes", "edicao" }, allowSetters = true)
    private Play play;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Resposta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getRespostaData() {
        return this.respostaData;
    }

    public Resposta respostaData(ZonedDateTime respostaData) {
        this.setRespostaData(respostaData);
        return this;
    }

    public void setRespostaData(ZonedDateTime respostaData) {
        this.respostaData = respostaData;
    }

    public Alternativa getRespostaAlternativa() {
        return this.respostaAlternativa;
    }

    public Resposta respostaAlternativa(Alternativa respostaAlternativa) {
        this.setRespostaAlternativa(respostaAlternativa);
        return this;
    }

    public void setRespostaAlternativa(Alternativa respostaAlternativa) {
        this.respostaAlternativa = respostaAlternativa;
    }

    public Boolean getRespostaCerta() {
        return this.respostaCerta;
    }

    public Resposta respostaCerta(Boolean respostaCerta) {
        this.setRespostaCerta(respostaCerta);
        return this;
    }

    public void setRespostaCerta(Boolean respostaCerta) {
        this.respostaCerta = respostaCerta;
    }

    public Questao getQuestao() {
        return this.questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public Resposta questao(Questao questao) {
        this.setQuestao(questao);
        return this;
    }

    public Participante getParticipante() {
        return this.participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Resposta participante(Participante participante) {
        this.setParticipante(participante);
        return this;
    }

    public Play getPlay() {
        return this.play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Resposta play(Play play) {
        this.setPlay(play);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resposta)) {
            return false;
        }
        return id != null && id.equals(((Resposta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resposta{" +
            "id=" + getId() +
            ", respostaData='" + getRespostaData() + "'" +
            ", respostaAlternativa='" + getRespostaAlternativa() + "'" +
            ", respostaCerta='" + getRespostaCerta() + "'" +
            "}";
    }
}
