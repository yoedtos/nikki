package net.yoedtos.nikki.external.view;

import static net.yoedtos.nikki.main.config.Constants.ENTER_FXML;
import static net.yoedtos.nikki.main.config.Constants.MAIN_FXML;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.OrderWith;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;

@RunWith(Enclosed.class)
public class I18nGUITest {

    @OrderWith(Alphanumeric.class)
    public static class EnterFxTest extends UIBaseTest {

        @Override
        public void start(Stage stage) throws Exception {
            var fxmlHelper = new FxmlHelper(ENTER_FXML, Locale.getDefault());
            var enterFxController = new EnterFxController.Builder().build();
            fxmlHelper.getFxmlLoader().setController(enterFxController);
            stage.setScene(fxmlHelper.createScene());
            stage.show();
            enterFxController.onStageDefined(null);
        }

        @Test
        public void A_shouldShowEntryTitleInEnUS() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("User Credentials", isVisible());

            Locale.setDefault(new Locale("pt", "BR"));
        }

        @Test
        public void B_shouldShowEntryTitleInPtBr() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("Dados de usuário", isVisible());

            Locale.setDefault(new Locale("ja", "JP"));
        }

        @Test
        public void C_shouldShowEntryTitleInJaJP() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("ユーザー認証情報", isVisible());

            Locale.setDefault(new Locale("en", "US"));
        }
    }

    @OrderWith(Alphanumeric.class)
    public static class MainFxTest extends UIBaseTest {

        @Override
        public void start(Stage stage) throws Exception {
            var fxmlHelper = new FxmlHelper(MAIN_FXML, Locale.getDefault());
            var mainFxController = new MainFxController.Builder().build();
            fxmlHelper.getFxmlLoader().setController(mainFxController);
            stage.setScene(fxmlHelper.createScene());
            stage.show();
        }

        @Before
        public void setup() {
            clickOn("#tabCreate");
        }

        @Test
        public void A_shouldShowTitleAndContentInEnUS() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("Title", isVisible());
            verifyThat("Content", isVisible());

            Locale.setDefault(new Locale("pt", "BR"));
        }

        @Test
        public void B_shouldShowTitleAndContentInPtBr() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("Título", isVisible());
            verifyThat("Conteúdo", isVisible());

            Locale.setDefault(new Locale("ja", "JP"));
        }

        @Test
        public void C_shouldShowTitleAndContentInJaJP() {
            WaitForAsyncUtils.waitForFxEvents();
            verifyThat("題名", isVisible());
            verifyThat("内容", isVisible());

            Locale.setDefault(new Locale("en", "US"));
        }
    }
}
