package net.yoedtos.nikki.external.view;

import static net.yoedtos.nikki.main.config.Constants.MAIN_APP_TITLE;
import static net.yoedtos.nikki.main.config.Constants.MAIN_FXML;
import static net.yoedtos.nikki.main.factories.Factory.Operation.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.*;
import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import net.yoedtos.nikki.main.factories.Factory;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Locale;
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
            createMainAndShow(stage);
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

    private void createMainAndShow(Stage stage) throws Exception {
        if (stage != null) {
            var fxmlHelper = new FxmlHelper(MAIN_FXML, Locale.getDefault());
            Factory factory = Factory.getInstance();
            var mainFxController = new MainFxController.Builder()
                    .createNoteController((CreateNoteController) factory.makeController(CREATE))
                    .dropNoteController((DropNoteController) factory.makeController(DROP))
                    .loadNoteController((LoadNotesPresenter) factory.makePresenter(LOAD))
                    .updateNoteController((UpdateNoteController) factory.makeController(UPDATE))
                    .build();
            fxmlHelper.getFxmlLoader().setController(mainFxController);
            stage.setTitle(MAIN_APP_TITLE);
            stage.setScene(fxmlHelper.createScene());
            stage.setResizable(false);
            stage.showAndWait();
            mainFxController.onStageDefined(stage);
        }
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
