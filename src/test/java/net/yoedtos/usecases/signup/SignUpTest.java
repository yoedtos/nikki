package net.yoedtos.usecases.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.entities.UserData;
import net.yoedtos.entities.error.ExistingUserError;
import net.yoedtos.entities.error.InvalidEmailError;
import net.yoedtos.entities.error.InvalidPasswordError;
import net.yoedtos.usecases.signup.ports.Encoder;
import net.yoedtos.usecases.signup.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

public class SignUpTest {
    private final String validEmail = "my@mail.com";
    private final String validPassword = "123Password";
    private final String encodedPassword = "ENCRYPTED";
    private final String invalidEmail = "invalid_email";
    private final String invalidPassword = "Password";
    private UserData userSignUpRequest;
    private SignUp signUpUseCase;
    private UserRepository userRepository;
    @Mock
    private Encoder mockEncoder;


    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        userSignUpRequest = new UserData(null, validEmail, validPassword);
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        signUpUseCase = new SignUp(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignUpUserWithValidData() {
        when(mockEncoder.encode(validPassword)).thenReturn(encodedPassword);
        var userSignUpResponse = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(userSignUpResponse.get().getId()).isEqualTo(0);
        assertThat(userRepository.findAllUsers().get().size()).isEqualTo(1);
        assertThat(userRepository.findUserByEmail(validEmail).get().getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    public void shouldNotSignUpAgainExistingUser() {
        userRepository.addUser(userSignUpRequest);
        var error = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(error.getLeft()).isEqualTo(new ExistingUserError(userSignUpRequest));
    }

    @Test
    public void shouldNotSignUpUserWithInvalidEmail() {
        var user = signUpUseCase.perform(new UserData(null, invalidEmail, validPassword)).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidEmailError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid email: " + invalidEmail + ".");
    }

    @Test
    public void shouldNotSignUpUserWithInvalidPassword() {
        var user = signUpUseCase.perform(new UserData(null, validEmail, invalidPassword)).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidPasswordError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid password.");
    }
}
