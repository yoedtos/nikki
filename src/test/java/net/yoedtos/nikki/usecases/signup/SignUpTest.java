package net.yoedtos.nikki.usecases.signup;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.nikki.builders.UserBuilder;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import net.yoedtos.nikki.entities.error.InvalidEmailError;
import net.yoedtos.nikki.entities.error.InvalidPasswordError;
import net.yoedtos.nikki.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.nikki.usecases.ports.Encoder;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;
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
        userSignUpRequest = UserBuilder.create().build();
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        signUpUseCase = new SignUp(userRepository, mockEncoder);
    }

    @Test
    public void shouldSignUpUserWithValidData() {
        when(mockEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        var userSignUpResponse = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(userSignUpResponse.get().getId()).isEqualTo(0);
        assertThat(userRepository.findAll().size()).isEqualTo(1);
        assertThat(userRepository.findByEmail(VALID_EMAIL).getPassword()).isEqualTo(ENCODED_PASSWORD);
    }

    @Test
    public void shouldNotSignUpAgainExistingUser() {
        userRepository.add(userSignUpRequest);
        var error = signUpUseCase.perform(userSignUpRequest).get();
        assertThat(error.getLeft()).isEqualTo(new ExistingUserError(userSignUpRequest));
    }

    @Test
    public void shouldNotSignUpUserWithInvalidEmail() {
        var invalidUser = UserBuilder.create().withInvalidEmail().build();
        var user = signUpUseCase.perform(invalidUser).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidEmailError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid email: " + INVALID_EMAIL + ".");
    }

    @Test
    public void shouldNotSignUpUserWithInvalidPassword() {
        var invalidUser = UserBuilder.create().withInvalidPassword().build();
        var user = signUpUseCase.perform(invalidUser).get();
        assertThat(user.getLeft()).isExactlyInstanceOf(InvalidPasswordError.class);
        assertThat(user.getLeft().getMessage()).isEqualTo("Invalid password.");
    }
}
