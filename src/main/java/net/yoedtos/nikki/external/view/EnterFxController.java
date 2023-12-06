package net.yoedtos.nikki.external.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.SignInController;
import net.yoedtos.nikki.controllers.SignUpController;
import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EnterFxController extends ViewFx {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterFxController.class);

    @FXML private CheckBox ckBxRegister;
    @FXML private TextField txtEmail;
    @FXML private PasswordField pwdPassword;

    private Stage stage;
    private SignUpController signUpController;
    private SignInController signInController;

    private EnterFxController(Builder builder) {
        this.signInController = builder.signInController;
        this.signUpController = builder.signUpController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.debug("{} init completed", EnterFxController.class.getSimpleName());
    }

    @Override
    public void onStageDefined(Stage stage) {
        this.stage = stage;
    }

    public void setSignInController(SignInController signInController) {
        this.signInController = signInController;
    }

    public void setSignUpController(SignUpController signUpController) {
        this.signUpController = signUpController;
    }

    @FXML
    public void onApply() {
        var enterDTO = new EnterDTO(txtEmail.getText(), pwdPassword.getText());
        Controller<EnterDTO> controller = signInController;

        if (ckBxRegister.isSelected()) {
            controller = signUpController;
        }

        try {
            controller.handle(enterDTO);
            LOGGER.debug("Controller: {}", getControllerName(controller));
        } catch (ExistingUserError e) {
            LOGGER.error(e.getMessage());
            showWarning(e.getMessage());
        } catch (Error | Exception e) {
            LOGGER.error(e.getMessage());
            showError(e.getMessage());
        }
    }

    @FXML
    public void onCancel() {
        Platform.exit();
        System.exit(0);
        LOGGER.debug("Canceling enter");
    }

    private String getControllerName(Controller controller) {
        return controller instanceof SignInController ?
                SignInController.class.getSimpleName() :
                SignUpController.class.getSimpleName();
    }

    public static class Builder {
        private SignUpController signUpController;
        private SignInController signInController;

        public Builder() {}

        public Builder signUpController(SignUpController signUpController) {
            this.signUpController = signUpController;
            return this;
        }
        public Builder signInController(SignInController signInController) {
            this.signInController = signInController;
            return this;
        }

        public EnterFxController build() {
            return new EnterFxController(this);
        }
    }
}
