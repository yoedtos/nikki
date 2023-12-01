package net.yoedtos.nikki.controllers;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.entities.error.WrongPasswordError;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.signin.SignIn;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SignInControllerTest {
    @Mock
    private SignIn signInUseCase;

    @InjectMocks
    private SignInController controller;

    private EnterDTO enterDTO;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        enterDTO = new EnterDTO(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void shouldSignInUserWithSuccessAndReturnUserEmail() {
        when(signInUseCase.perform(any())).thenReturn(Future.of(() -> Either.right(DB_USER)));
        controller.handle(enterDTO);
        assertThat(MainApp.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(MainApp.getUserEmail()).isEqualTo(VALID_EMAIL);
    }

    @Test
    public void shouldNotSignInWithInvalidPassword() {
        when(signInUseCase.perform(any())).thenReturn(Future.of(() -> Either.left(new WrongPasswordError())));
        assertThatThrownBy(() -> {
            controller.handle(enterDTO);
        }).isInstanceOf(WrongPasswordError.class);
    }

    @Test
    public void shouldNotSignInUnregisteredUser() {
        when(signInUseCase.perform(any())).thenReturn(Future.of(() -> Either.left(new UserNotFoundError())));
        assertThatThrownBy(() -> controller.handle(enterDTO)).isInstanceOf(UserNotFoundError.class);
    }
}
