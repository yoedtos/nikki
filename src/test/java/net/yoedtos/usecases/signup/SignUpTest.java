package net.yoedtos.usecases.signup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.vavr.control.Either;
import net.yoedtos.entities.UserData;
import net.yoedtos.entities.error.ExistingUserError;
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
    private UserData userSignUpRequest;
    private SignUp signUpUseCase;
    private UserRepository userRepository;
    @Mock
    private Encoder mockEncoder;


    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        userSignUpRequest = new UserData(validEmail, validPassword);
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        signUpUseCase = new SignUp(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignUpUserWithValidData() {
        when(mockEncoder.encode(validPassword)).thenReturn(encodedPassword);
        var userSignUpResponse = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(userSignUpResponse.get()).isEqualTo(userSignUpRequest);
        assertThat(userRepository.findAllUsers().get().size()).isEqualTo(1);
        assertThat(userRepository.findUserByEmail(validEmail).get().getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    public void shouldNotSignUpAgainExistingUser() {
        userRepository.addUser(userSignUpRequest);
        var error = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(error.getLeft()).isEqualTo(new ExistingUserError(userSignUpRequest));
    }
}
