package net.yoedtos.nikki.entities;

import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.nikki.entities.Password;
import org.junit.Test;

public class PasswordTest {

    @Test
    public void shouldNotAcceptNullStrings() {
        assertThat(Password.validate(null)).isFalse();
    }

    @Test
    public void shouldNotAcceptEmptyStrings() {
        assertThat(Password.validate("")).isFalse();
    }

    @Test
    public void shouldNotAcceptNumberLess() {
        assertThat(Password.validate("ABCDabcd")).isFalse();
    }

    @Test
    public void shouldNotAcceptPasswordSizeLessThenSix() {
        assertThat(Password.validate("12ABa")).isFalse();
    }
}
