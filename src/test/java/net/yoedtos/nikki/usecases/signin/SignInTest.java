package net.yoedtos.nikki.usecases.signin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.nikki.builders.UserBuilder;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.entities.error.WrongPasswordError;
import net.yoedtos.nikki.usecases.TestConstant;
import net.yoedtos.nikki.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.nikki.usecases.ports.Encoder;
import net.yoedtos.nikki.usecases.ports.UserRepository;
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
        userRepository = new InMemoryUserRepository(new ArrayList<>(List.of(TestConstant.DB_USER)));
        signInUseCase = new SignIn(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignInWithCorrectPassword() {
        var validUser = UserBuilder.create().build();
        when(mockEncoder.compare(TestConstant.VALID_PASSWORD, TestConstant.ENCODED_PASSWORD)).thenReturn(true);
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
