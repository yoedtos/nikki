package net.yoedtos.nikki.it;

import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNIN;
import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNUP;
import static net.yoedtos.nikki.usecases.TestConstant.VALID_EMAIL;
import static net.yoedtos.nikki.usecases.TestConstant.VALID_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.controllers.SignInController;
import net.yoedtos.nikki.controllers.SignUpController;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.main.factories.Factory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OrderWith(Alphanumeric.class)
public class Controllers extends BaseIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controllers.class);

    private EnterDTO enterDTO;

    @Before
    public void init() {
        enterDTO = new EnterDTO(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    public void A_shouldNotSignInUnknownUser() {
        var signInController = (SignInController) Factory.getInstance().makeController(SIGNIN);
        var unknown = new EnterDTO("unknown@abc.com", VALID_PASSWORD);

        assertThatThrownBy(() -> signInController.handle(unknown))
                .isInstanceOf(UserNotFoundError.class)
                .hasMessage("User not found.");

        assertThat(MainApp.getUserId()).isNull();
        assertThat(MainApp.getUserEmail()).isNull();
    }

    @Test
    public void B_shouldSignUpCorrectly() {
        var signUpController = (SignUpController) Factory.getInstance().makeController(SIGNUP);
        signUpController.handle(enterDTO);
        var usedId = MainApp.getUserId();
        var userEmail = MainApp.getUserEmail();
        assertThat(usedId).isNotNull();
        assertThat(userEmail).isNotNull();
        LOGGER.debug("Id: {} - Email: {}", usedId, userEmail);
    }

    @Test
    public void C_shouldSignInCorrectly() {
        resetCredential();
        var signInController = (SignInController) Factory.getInstance().makeController(SIGNIN);
        signInController.handle(enterDTO);
        var usedId = MainApp.getUserId();
        var userEmail = MainApp.getUserEmail();
        assertThat(usedId).isNotNull();
        assertThat(userEmail).isNotNull();
        LOGGER.debug("Id: {} - Email: {}", usedId, userEmail);
    }
}
