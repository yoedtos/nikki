package net.yoedtos.nikki.controllers;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.builders.UserBuilder;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.signup.SignUp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class SignUpControllerTest {
    @Mock
    private SignUp signUpUseCase;

    @InjectMocks
    private SignUpController controller;

    private EnterDTO enterDTO;


    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        enterDTO = new EnterDTO(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void shouldSignUpUserWithSuccessAndReturnUserEmail() {
        when(signUpUseCase.perform(any())).thenReturn(Future.of(() -> Either.right(DB_USER)));
        controller.handle(enterDTO);
        assertThat(MainApp.getUserId()).isEqualTo(VALID_USER_ID);
        assertThat(MainApp.getUserEmail()).isEqualTo(VALID_EMAIL);
    }

    @Test
    public void shouldNotSignUpUserTwice() {
        var userData = UserBuilder.create().build();
        when(signUpUseCase.perform(any())).thenReturn(Future.of(() -> Either.left(new ExistingUserError(userData))));
        assertThatThrownBy(() -> {
            controller.handle(enterDTO);
        }).isInstanceOf(ExistingUserError.class);
    }
}
