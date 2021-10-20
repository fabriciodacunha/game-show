package com.padrinho.gameshow.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.padrinho.gameshow.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Play.class);
        Play play1 = new Play();
        play1.setId(1L);
        Play play2 = new Play();
        play2.setId(play1.getId());
        assertThat(play1).isEqualTo(play2);
        play2.setId(2L);
        assertThat(play1).isNotEqualTo(play2);
        play1.setId(null);
        assertThat(play1).isNotEqualTo(play2);
    }
}
