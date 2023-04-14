package net.yoedtos.usecases.signin;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.entities.error.WrongPasswordError;
import net.yoedtos.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.usecases.ports.Encoder;
import net.yoedtos.usecases.ports.UserData;
import net.yoedtos.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class SignInTest {
    private String wrongPassword = "drowssaP321";
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
        when(mockEncoder.compare(VALID_PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        var userResponse = signInUseCase.perform(VALID_USER).get();
        assertThat(userResponse.get()).isEqualTo(VALID_USER);
    }

    @Test
    public void shouldNotSignInWithIncorrectPassword() {
        var invalidUser = new UserData(null, VALID_EMAIL, wrongPassword);
        var response = signInUseCase.perform(invalidUser).get();
        var error = response.getLeft();
        assertThat(error).isExactlyInstanceOf(WrongPasswordError.class);
        assertThat(error.getMessage()).isEqualTo("Wrong password.");
    }
}
