package com.padrinho.gameshow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Edicao.
 */
@Entity
@Table(name = "edicao")
public class Edicao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "edicao_titulo")
    private String edicaoTitulo;

    @Column(name = "edicao_data")
    private LocalDate edicaoData;

    @JsonIgnoreProperties(value = { "respostas", "participantes", "edicao" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Play play;

    @OneToMany(mappedBy = "edicao")
    @JsonIgnoreProperties(value = { "respostas", "edicao" }, allowSetters = true)
    private Set<Questao> questaos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Edicao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEdicaoTitulo() {
        return this.edicaoTitulo;
    }

    public Edicao edicaoTitulo(String edicaoTitulo) {
        this.setEdicaoTitulo(edicaoTitulo);
        return this;
    }

    public void setEdicaoTitulo(String edicaoTitulo) {
        this.edicaoTitulo = edicaoTitulo;
    }

    public LocalDate getEdicaoData() {
        return this.edicaoData;
    }

    public Edicao edicaoData(LocalDate edicaoData) {
        this.setEdicaoData(edicaoData);
        return this;
    }

    public void setEdicaoData(LocalDate edicaoData) {
        this.edicaoData = edicaoData;
    }

    public Play getPlay() {
        return this.play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public Edicao play(Play play) {
        this.setPlay(play);
        return this;
    }

    public Set<Questao> getQuestaos() {
        return this.questaos;
    }

    public void setQuestaos(Set<Questao> questaos) {
        if (this.questaos != null) {
            this.questaos.forEach(i -> i.setEdicao(null));
        }
        if (questaos != null) {
            questaos.forEach(i -> i.setEdicao(this));
        }
        this.questaos = questaos;
    }

    public Edicao questaos(Set<Questao> questaos) {
        this.setQuestaos(questaos);
        return this;
    }

    public Edicao addQuestao(Questao questao) {
        this.questaos.add(questao);
        questao.setEdicao(this);
        return this;
    }

    public Edicao removeQuestao(Questao questao) {
        this.questaos.remove(questao);
        questao.setEdicao(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edicao)) {
            return false;
        }
        return id != null && id.equals(((Edicao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Edicao{" +
            "id=" + getId() +
            ", edicaoTitulo='" + getEdicaoTitulo() + "'" +
            ", edicaoData='" + getEdicaoData() + "'" +
            "}";
    }
}
