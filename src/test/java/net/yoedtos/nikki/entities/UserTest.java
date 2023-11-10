package net.yoedtos.nikki.entities;

import static io.vavr.control.Either.left;
import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.nikki.entities.User;
import net.yoedtos.nikki.entities.error.InvalidEmailError;
import net.yoedtos.nikki.entities.error.InvalidPasswordError;
import net.yoedtos.nikki.usecases.ports.UserData;
import org.junit.Test;

public class UserTest {

    @Test
    public void shouldNotCreateUserWithInvalidEmail() {
        var error = User.create(new UserData(null, INVALID_EMAIL, VALID_PASSWORD));
        assertThat(error).isEqualTo(left(new InvalidEmailError(INVALID_EMAIL)));
    }

    @Test
    public void shouldCreateUserWithValidData() {
        var user = User.create(new UserData(null, VALID_EMAIL, VALID_PASSWORD)).get();
        assertThat(user.getEmail().getValue()).isEqualTo(VALID_EMAIL);
    }

    @Test
    public void shouldNotCreateUserWithNumberLessPassword() {
        var error = User.create(new UserData(null, VALID_EMAIL, INVALID_PASSWORD));
        assertThat(error).isEqualTo(left(new InvalidPasswordError()));
    }
}
