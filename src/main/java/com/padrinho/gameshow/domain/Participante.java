package com.padrinho.gameshow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Participante.
 */
@Entity
@Table(name = "participante")
public class Participante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "carastro_usuario")
    private String carastroUsuario;

    @Column(name = "participante_nome")
    private String participanteNome;

    @Column(name = "participante_email")
    private String participanteEmail;

    @Column(name = "participante_data_de_nascimento")
    private LocalDate participanteDataDeNascimento;

    @OneToMany(mappedBy = "participante")
    @JsonIgnoreProperties(value = { "questao", "participante", "play" }, allowSetters = true)
    private Set<Resposta> respostas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "respostas", "participantes", "edicao" }, allowSetters = true)
    private Play play;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Participante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarastroUsuario() {
        return this.carastroUsuario;
    }

    public Participante carastroUsuario(String carastroUsuario) {
        this.setCarastroUsuario(carastroUsuario);
        return this;
    }

    public void setCarastroUsuario(String carastroUsuario) {
        this.carastroUsuario = carastroUsuario;
    }

    public String getParticipanteNome() {
        return this.participanteNome;
    }

    public Participante participanteNome(String participanteNome) {
        this.setParticipanteNome(participanteNome);
        return this;
    }

    public void setParticipanteNome(String participanteNome) {
        this.participanteNome = participanteNome;
    }

    public String getParticipanteEmail() {
        return this.participanteEmail;
    }

    public Participante participanteEmail(String participanteEmail) {
        this.setParticipanteEmail(participanteEmail);
        return this;
    }

    public void setParticipanteEmail(String participanteEmail) {
        this.participanteEmail = participanteEmail;
    }

    public LocalDate getParticipanteDataDeNascimento() {
        return this.participanteDataDeNascimento;
    }

    public Participante participanteDataDeNascimento(LocalDate participanteDataDeNascimento) {
        this.setParticipanteDataDeNascimento(participanteDataDeNascimento);
        return this;
    }

    public void setParticipanteDataDeNascimento(LocalDate participanteDataDeNascimento) {
        this.participanteDataDeNascimento = participanteDataDeNascimento;
    }

    public Set<Resposta> getRespostas() {
        return this.respostas;
    }

    public void setRespostas(Set<Resposta> respostas) {
        if (this.respostas != null) {
            this.respostas.forEach(i -> i.setParticipante(null));
        }
        if (respostas != null) {
            respostas.forEach(i -> i.setParticipante(this));
        }
        this.respostas = respostas;
    }

    public Participante respostas(Set<Resposta> respostas) {
        this.setRespostas(respostas);
        return this;
    }

    public Participante addResposta(Resposta resposta) {
        this.respostas.add(resposta);
        resposta.setParticipante(this);
        return this;
    }

    public Participante removeResposta(Resposta resposta) {
        this.respostas.remove(resposta);
        resposta.setParticipante(null);
        return this;
    }

    public Play getPlay() {
        return this.play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Participante play(Play play) {
        this.setPlay(play);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participante)) {
            return false;
        }
        return id != null && id.equals(((Participante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participante{" +
            "id=" + getId() +
            ", carastroUsuario='" + getCarastroUsuario() + "'" +
            ", participanteNome='" + getParticipanteNome() + "'" +
            ", participanteEmail='" + getParticipanteEmail() + "'" +
            ", participanteDataDeNascimento='" + getParticipanteDataDeNascimento() + "'" +
            "}";
    }
}
