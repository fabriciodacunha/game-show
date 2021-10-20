package com.padrinho.gameshow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.padrinho.gameshow.domain.enumeration.Alternativa;
import com.padrinho.gameshow.domain.enumeration.Assunto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Questao.
 */
@Entity
@Table(name = "questao")
public class Questao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "questao_titulo")
    private String questaoTitulo;

    @Column(name = "alternativa_a")
    private String alternativaA;

    @Column(name = "alternativa_b")
    private String alternativaB;

    @Column(name = "alternativa_c")
    private String alternativaC;

    @Column(name = "alternativa_d")
    private String alternativaD;

    @Column(name = "nivel_idade")
    private Integer nivelIdade;

    @Enumerated(EnumType.STRING)
    @Column(name = "assunto")
    private Assunto assunto;

    @Enumerated(EnumType.STRING)
    @Column(name = "alternativa_certa")
    private Alternativa alternativaCerta;

    @OneToMany(mappedBy = "questao")
    @JsonIgnoreProperties(value = { "questao", "participante", "play" }, allowSetters = true)
    private Set<Resposta> respostas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "play", "questaos" }, allowSetters = true)
    private Edicao edicao;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestaoTitulo() {
        return this.questaoTitulo;
    }

    public Questao questaoTitulo(String questaoTitulo) {
        this.setQuestaoTitulo(questaoTitulo);
        return this;
    }

    public void setQuestaoTitulo(String questaoTitulo) {
        this.questaoTitulo = questaoTitulo;
    }

    public String getAlternativaA() {
        return this.alternativaA;
    }

    public Questao alternativaA(String alternativaA) {
        this.setAlternativaA(alternativaA);
        return this;
    }

    public void setAlternativaA(String alternativaA) {
        this.alternativaA = alternativaA;
    }

    public String getAlternativaB() {
        return this.alternativaB;
    }

    public Questao alternativaB(String alternativaB) {
        this.setAlternativaB(alternativaB);
        return this;
    }

    public void setAlternativaB(String alternativaB) {
        this.alternativaB = alternativaB;
    }

    public String getAlternativaC() {
        return this.alternativaC;
    }

    public Questao alternativaC(String alternativaC) {
        this.setAlternativaC(alternativaC);
        return this;
    }

    public void setAlternativaC(String alternativaC) {
        this.alternativaC = alternativaC;
    }

    public String getAlternativaD() {
        return this.alternativaD;
    }

    public Questao alternativaD(String alternativaD) {
        this.setAlternativaD(alternativaD);
        return this;
    }

    public void setAlternativaD(String alternativaD) {
        this.alternativaD = alternativaD;
    }

    public Integer getNivelIdade() {
        return this.nivelIdade;
    }

    public Questao nivelIdade(Integer nivelIdade) {
        this.setNivelIdade(nivelIdade);
        return this;
    }

    public void setNivelIdade(Integer nivelIdade) {
        this.nivelIdade = nivelIdade;
    }

    public Assunto getAssunto() {
        return this.assunto;
    }

    public Questao assunto(Assunto assunto) {
        this.setAssunto(assunto);
        return this;
    }

    public void setAssunto(Assunto assunto) {
        this.assunto = assunto;
    }

    public Alternativa getAlternativaCerta() {
        return this.alternativaCerta;
    }

    public Questao alternativaCerta(Alternativa alternativaCerta) {
        this.setAlternativaCerta(alternativaCerta);
        return this;
    }

    public void setAlternativaCerta(Alternativa alternativaCerta) {
        this.alternativaCerta = alternativaCerta;
    }

    public Set<Resposta> getRespostas() {
        return this.respostas;
    }

    public void setRespostas(Set<Resposta> respostas) {
        if (this.respostas != null) {
            this.respostas.forEach(i -> i.setQuestao(null));
        }
        if (respostas != null) {
            respostas.forEach(i -> i.setQuestao(this));
        }
        this.respostas = respostas;
    }

    public Questao respostas(Set<Resposta> respostas) {
        this.setRespostas(respostas);
        return this;
    }

    public Questao addResposta(Resposta resposta) {
        this.respostas.add(resposta);
        resposta.setQuestao(this);
        return this;
    }

    public Questao removeResposta(Resposta resposta) {
        this.respostas.remove(resposta);
        resposta.setQuestao(null);
        return this;
    }

    public Edicao getEdicao() {
        return this.edicao;
    }

    public void setEdicao(Edicao edicao) {
        this.edicao = edicao;
    }

    public Questao edicao(Edicao edicao) {
        this.setEdicao(edicao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questao)) {
            return false;
        }
        return id != null && id.equals(((Questao) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questao{" +
            "id=" + getId() +
            ", questaoTitulo='" + getQuestaoTitulo() + "'" +
            ", alternativaA='" + getAlternativaA() + "'" +
            ", alternativaB='" + getAlternativaB() + "'" +
            ", alternativaC='" + getAlternativaC() + "'" +
            ", alternativaD='" + getAlternativaD() + "'" +
            ", nivelIdade=" + getNivelIdade() +
            ", assunto='" + getAssunto() + "'" +
            ", alternativaCerta='" + getAlternativaCerta() + "'" +
            "}";
    }
}
