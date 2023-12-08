package net.yoedtos.nikki.main;

import static net.yoedtos.nikki.main.config.Constants.ENTER_FXML;
import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNIN;
import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNUP;

import javafx.application.Application;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.SignInController;
import net.yoedtos.nikki.controllers.SignUpController;
import net.yoedtos.nikki.external.view.EnterFxController;
import net.yoedtos.nikki.external.view.FxmlHelper;
import net.yoedtos.nikki.main.factories.Factory;

import java.util.Locale;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        var fxmlHelper = new FxmlHelper(ENTER_FXML, Locale.getDefault());
        Factory factory = Factory.getInstance();
        var enterFxController = new EnterFxController.Builder()
                .signInController((SignInController) factory.makeController(SIGNIN))
                .signUpController((SignUpController) factory.makeController(SIGNUP))
                .build();
        fxmlHelper.getFxmlLoader().setController(enterFxController);
        stage.setResizable(false);
        stage.setScene(fxmlHelper.createScene());
        stage.show();
        enterFxController.onStageDefined(new Stage());
    }

    public static void begin() {
        launch();
    }
}