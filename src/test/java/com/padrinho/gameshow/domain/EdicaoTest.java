package com.padrinho.gameshow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.padrinho.gameshow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EdicaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Edicao.class);
        Edicao edicao1 = new Edicao();
        edicao1.setId(1L);
        Edicao edicao2 = new Edicao();
        edicao2.setId(edicao1.getId());
        assertThat(edicao1).isEqualTo(edicao2);
        edicao2.setId(2L);
        assertThat(edicao1).isNotEqualTo(edicao2);
        edicao1.setId(null);
        assertThat(edicao1).isNotEqualTo(edicao2);
    }
}
