package net.yoedtos.nikki.external.view;

import static net.yoedtos.nikki.main.config.Constants.ENTER_FXML;
import static net.yoedtos.nikki.usecases.TestConstant.VALID_EMAIL;
import static net.yoedtos.nikki.usecases.TestConstant.VALID_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.SignInController;
import net.yoedtos.nikki.controllers.SignUpController;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import net.yoedtos.nikki.usecases.ports.UserData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.service.query.EmptyNodeQueryException;

import java.util.Locale;

public class EnterFxControllerTest extends UIBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterFxControllerTest.class);

    private EnterFxController enterFxController;

    @Override
    public void start(Stage stage) throws Exception {
        var fxmlHelper = new FxmlHelper(ENTER_FXML, Locale.getDefault());
        enterFxController = new EnterFxController.Builder().build();
        fxmlHelper.getFxmlLoader().setController(enterFxController);
        stage.setScene(fxmlHelper.createScene());
        stage.show();
        enterFxController.onStageDefined(null);
    }

    @Test
    public void shouldReturnUserIdWhenSignInSuccess() {
        var signInController = new SignInController(null) {

            @Override
            public void handle(EnterDTO enterDTO) {
                LOGGER.info("SignIn data: {}", enterDTO);
                assertThat(enterDTO.getEmail()).isEqualTo(VALID_EMAIL);
                assertThat(enterDTO.getPassword()).isEqualTo(VALID_PASSWORD);
            }
        };
        enterFxController.setSignInController(signInController);
        clickOn("#txtEmail").write(VALID_EMAIL);
        clickOn("#pwdPassword").write(VALID_PASSWORD);
        clickOn("#btnApply");

        assertThatThrownBy(() -> {
            getNode("Error", Label.class);
        }).isInstanceOf(EmptyNodeQueryException.class);
    }

    @Test
    public void shouldReturnUserIdWhenSignUpSuccess() {
        var signUpController = new SignUpController(null) {

            @Override
            public void handle(EnterDTO enterDTO) {
                LOGGER.info("SignUp data: {}", enterDTO);
                assertThat(enterDTO.getEmail()).isEqualTo(VALID_EMAIL);
                assertThat(enterDTO.getPassword()).isEqualTo(VALID_PASSWORD);
            }
        };
        enterFxController.setSignUpController(signUpController);
        clickOn("#txtEmail").write(VALID_EMAIL);
        clickOn("#pwdPassword").write(VALID_PASSWORD);
        clickOn("#ckBxRegister");
        clickOn("#btnApply");

        assertThatThrownBy(() -> {
            getNode("Error", Label.class);
        }).isInstanceOf(EmptyNodeQueryException.class);
    }

    @Test
    public void shouldShowWarningWhenSignUpUserTwice() {
        var signUpController = new SignUpController(null) {

            @Override
            public void handle(EnterDTO enterDTO) {
                LOGGER.info("SignUp data: {}", enterDTO);
                throw new ExistingUserError(new UserData(null, VALID_EMAIL, VALID_PASSWORD));
            }
        };
        enterFxController.setSignUpController(signUpController);
        clickOn("#txtEmail").write(VALID_EMAIL);
        clickOn("#pwdPassword").write(VALID_PASSWORD);
        clickOn("#ckBxRegister");
        clickOn("#btnApply");
        verifyThat("User " + VALID_EMAIL + " already registered.", isVisible());
    }
}
