package net.yoedtos.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EmailTest {

    @Test
    public void shouldNotAcceptNullStrings() {
        assertThat(Email.validate(null)).isFalse();
    }

    @Test
    public void shouldNotAcceptEmptyStrings() {
        assertThat(Email.validate("")).isFalse();
    }

    @Test
    public void shouldNotAcceptLocalPartLargerThen64Chars() {
        var localLarge = "l".repeat(65) + "@mail.com";
        assertThat(Email.validate(localLarge)).isFalse();
    }

    @Test
    public void shouldNotAcceptDomainPartLargerThen255Chars() {
        var domainLarge = "local@" + "d".repeat(128) + "." + "d".repeat(127);
        assertThat(Email.validate(domainLarge)).isFalse();
    }

    @Test
    public void shouldNotAcceptLocalPartEmpty() {
        var localEmpty = "@mail.com";
        assertThat(Email.validate(localEmpty)).isFalse();
    }

    @Test
    public void shouldNotAcceptDomainPartEmpty() {
        var domainEmpty = "local@";
        assertThat(Email.validate(domainEmpty)).isFalse();
    }

    @Test
    public void shouldNotAcceptFirstDomainPartLargerThen63Chars() {
        var firstDomainLarge = "local@" + "d".repeat(64) + ".com";
        assertThat(Email.validate(firstDomainLarge)).isFalse();
    }

    @Test
    public void shouldNotAcceptLocalPartWithInvalidChar() {
        var invalidChar = "any email@mail.com";
        assertThat(Email.validate(invalidChar)).isFalse();
    }

    @Test
    public void shouldNotAcceptLocalPartWithTwoDots() {
        var twoDot = "anyemail..@mail.com";
        assertThat(Email.validate(twoDot)).isFalse();
    }
    @Test
    public void shouldNotAcceptLocalPartWithEndingDot() {
        var endingDot = "anyemail.@mail.com";
        assertThat(Email.validate(endingDot)).isFalse();
    }

    @Test
    public void shouldNotAcceptEmailWithoutAnAtSign() {
        var withoutAtSign = "anyemailmail.com";
        assertThat(Email.validate(withoutAtSign)).isFalse();
    }

    @Test
    public void shouldAcceptValidEmail() {
        var email = "anyemail@mail.com";
        assertThat(Email.validate(email)).isTrue();
    }
}
