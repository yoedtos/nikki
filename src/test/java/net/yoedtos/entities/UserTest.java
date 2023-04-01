package net.yoedtos.entities;

import static io.vavr.control.Either.left;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.entities.error.InvalidEmailError;
import org.junit.Test;

public class UserTest {

    @Test
    public void shouldNotCreateUserWithInvalidEmail() {
        String invalidEmail = "invalid_email";
        var error = User.create(new UserData(invalidEmail));
        assertThat(error).isEqualTo(left(new InvalidEmailError()));
    }

    @Test
    public void shouldCreateUserWithValidData() {
        String validEmail = "any@email.com";
        var user = User.create(new UserData(validEmail)).get();
        assertThat(user.email.value).isEqualTo(validEmail);
    }
}
