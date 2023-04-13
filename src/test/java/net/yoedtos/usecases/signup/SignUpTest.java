package net.yoedtos.usecases.signup;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.entities.error.ExistingUserError;
import net.yoedtos.entities.error.InvalidEmailError;
import net.yoedtos.entities.error.InvalidPasswordError;
import net.yoedtos.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.usecases.ports.Encoder;
import net.yoedtos.usecases.ports.UserData;
import net.yoedtos.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

public class SignUpTest {
    private UserData userSignUpRequest;
    private SignUp signUpUseCase;
    private UserRepository userRepository;
    @Mock
    private Encoder mockEncoder;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        userSignUpRequest = new UserData(null, VALID_EMAIL, VALID_PASSWORD);
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        signUpUseCase = new SignUp(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignUpUserWithValidData() {
        when(mockEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        var userSignUpResponse = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(userSignUpResponse.get().getId()).isEqualTo(0);
        assertThat(userRepository.findAllUsers().size()).isEqualTo(1);
        assertThat(userRepository.findUserByEmail(VALID_EMAIL).getPassword()).isEqualTo(ENCODED_PASSWORD);
    }

    @Test
    public void shouldNotSignUpAgainExistingUser() {
        userRepository.addUser(userSignUpRequest);
        var error = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(error.getLeft()).isEqualTo(new ExistingUserError(userSignUpRequest));
    }

    @Test
    public void shouldNotSignUpUserWithInvalidEmail() {
        var user = signUpUseCase.perform(new UserData(null, INVALID_EMAIL, VALID_PASSWORD)).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidEmailError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid email: " + INVALID_EMAIL + ".");
    }

    @Test
    public void shouldNotSignUpUserWithInvalidPassword() {
        var user = signUpUseCase.perform(new UserData(null, VALID_EMAIL, INVALID_PASSWORD)).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidPasswordError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid password.");
    }
}
