package net.yoedtos.usecases.signin;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.builders.UserBuilder;
import net.yoedtos.entities.error.UserNotFoundError;
import net.yoedtos.entities.error.WrongPasswordError;
import net.yoedtos.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.usecases.ports.Encoder;
import net.yoedtos.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class SignInTest {
    private UserRepository userRepository;
    private SignIn signInUseCase;

    @Mock
    private Encoder mockEncoder;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        userRepository = new InMemoryUserRepository(new ArrayList<>(List.of(DB_USER)));
        signInUseCase = new SignIn(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignInWithCorrectPassword() {
        var validUser = UserBuilder.create().build();
        when(mockEncoder.compare(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        var userResponse = signInUseCase.perform(validUser).get();
        assertThat(userResponse.get()).isEqualTo(validUser);
    }

    @Test
    public void shouldNotSignInWithIncorrectPassword() {
        var invalidUser = UserBuilder.create().withWrongPassword().build();
        var response = signInUseCase.perform(invalidUser).get();
        var error = response.getLeft();
        assertThat(error).isExactlyInstanceOf(WrongPasswordError.class);
        assertThat(error.getMessage()).isEqualTo("Wrong password.");
    }

    @Test
    public void shouldNotSignInWithUnregisteredUser() {
        var unRegisteredUser = UserBuilder.create().withUnregisteredEmail().build();
        var response = signInUseCase.perform(unRegisteredUser).get();
        var error = response.getLeft();
        assertThat(error).isExactlyInstanceOf(UserNotFoundError.class);
        assertThat(error.getMessage()).isEqualTo("User not found.");
    }
}
