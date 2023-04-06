package net.yoedtos.entities;

import static io.vavr.control.Either.left;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.entities.error.InvalidEmailError;
import net.yoedtos.entities.error.InvalidPasswordError;
import org.junit.Test;

public class UserTest {

    private String validPassword = "123Password";

    @Test
    public void shouldNotCreateUserWithInvalidEmail() {
        String invalidEmail = "invalid_email";
        var error = User.create(new UserData(invalidEmail, validPassword));
        assertThat(error).isEqualTo(left(new InvalidEmailError(invalidEmail)));
    }

    @Test
    public void shouldCreateUserWithValidData() {
        String validEmail = "any@email.com";
        var user = User.create(new UserData(validEmail, validPassword)).get();
        assertThat(user.getEmail().getValue()).isEqualTo(validEmail);
    }

    @Test
    public void shouldNotCreateUserWithNumberLessPassword() {
        String validEmail = "any@email.com";
        String invalidPassword = "password";
        var error = User.create(new UserData(validEmail, invalidPassword));
        assertThat(error).isEqualTo(left(new InvalidPasswordError()));
    }
}
