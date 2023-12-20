package net.yoedtos.nikki.it;

import static net.yoedtos.nikki.main.config.Constants.ENTER_FXML;
import static net.yoedtos.nikki.main.config.Constants.MAIN_APP_TITLE;
import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNIN;
import static net.yoedtos.nikki.main.factories.Factory.Operation.SIGNUP;
import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.SignInController;
import net.yoedtos.nikki.controllers.SignUpController;
import net.yoedtos.nikki.external.view.EnterFxController;
import net.yoedtos.nikki.external.view.FxmlHelper;
import net.yoedtos.nikki.main.factories.Factory;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;

public class UserInterface extends ApplicationTest {

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

    @Before
    public void loginIn() {
        clickOn("#txtEmail").write(VALID_EMAIL);
        clickOn("#pwdPassword").write(VALID_PASSWORD);
        clickOn("#btnApply");

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void shouldEnterAndNavigateThroughTabs() {
        assertThat(targetWindow(MAIN_APP_TITLE)).isNotNull();
        clickOn("#tabCreate").clickOn("#tabList");
        verifyThat(VALID_EMAIL, isVisible());
        clickOn("#tabAbout").clickOn("#tabHome");
    }

    @Test
    public void shouldCreateNoteAfterViewNote() {
        clickOn("#tabList").clickOn(TITLE_ONE);

        clickOn("#tabCreate").clickOn("#txtTitle").write(TITLE_MOD);
        clickOn("#htmlContent").write(CONTENT_MOD);
        clickOn("#btnSave");

        clickOn("#tabList");
        var noteList = lookup("#viewNotes").queryListView().getItems();
        assertThat(noteList).hasSize(3).asString().contains(TITLE_ONE, TITLE_TWO, TITLE_MOD);
    }

    @Test
    public void shouldCreateNoteAfterDropNote() {
        clickOn("#tabList").clickOn(TITLE_TWO).clickOn("#btnDrop");

        clickOn("#tabCreate").clickOn("#txtTitle").write(TITLE_TWO);
        clickOn("#htmlContent").write(CONTENT_TWO);
        clickOn("#btnSave");

        clickOn("#tabList");
        var noteList = lookup("#viewNotes").queryListView().getItems();
        assertThat(noteList).hasSize(2).asString().contains(TITLE_ONE, TITLE_TWO);
    }
}
