package net.yoedtos.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TitleTest {

    @Test
    public void shouldNotAcceptNullStrings() {
        assertThat(Title.validate(null)).isFalse();
    }

    @Test
    public void shouldNotAcceptEmptyStrings() {
        assertThat(Title.validate("")).isFalse();
    }

    @Test
    public void shouldNotAcceptTitleLargerThen256Chars() {
        var largeTitle = "t".repeat(257);
        assertThat(Title.validate(largeTitle)).isFalse();
    }
    @Test
    public void shouldNotAcceptTitleShorterThen3Chars() {
        var shortTitle = "ti";
        assertThat(Title.validate(shortTitle)).isFalse();
    }
}
