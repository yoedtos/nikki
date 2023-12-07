package net.yoedtos.nikki.external.view;

import static javafx.scene.input.KeyCode.BACK_SPACE;
import static net.yoedtos.nikki.main.config.Constants.MAIN_FXML;
import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.CreateNoteController;
import net.yoedtos.nikki.controllers.DropNoteController;
import net.yoedtos.nikki.controllers.UpdateNoteController;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testfx.service.query.EmptyNodeQueryException;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.Locale;

@RunWith(Enclosed.class)
public class MainFxControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFxControllerTest.class);

    protected final static int TAB_HOME = 0;
    protected final static int TAB_CREATE = 1;

    protected final static String TEXT_CONT = "Lorem ipsum dolor sit amet";
    protected final static String HTML_CONT = "<p>Lorem ipsum dolor sit amet</p>";

    public static class CreateTabTest extends UIBaseTest {

        private MainFxController mainController;

        @Override
        public void start(Stage stage) throws Exception {
            mainController = setTabAndShow(TAB_CREATE, stage);
        }

        @Test
        public void shouldSaveNoteWithSuccess() {
            var createNoteController = new CreateNoteController(null) {
                @Override
                public void handle(NoteDTO noteDTO) {
                    LOGGER.info("Note Data: {}", noteDTO);
                    assertThat(noteDTO.getTitle()).isEqualTo(VALID_TITLE);
                    assertThat(noteDTO.getContent()).contains(HTML_CONT);
                }
            };

            mainController.setCreateNoteController(createNoteController);
            clickOn("#txtTitle").write(VALID_TITLE);
            clickOn("#htmlContent").write(TEXT_CONT);
            clickOn("#btnSave");

            assertThatThrownBy(() -> {
                getNode("Error", Label.class);
            }).isInstanceOf(EmptyNodeQueryException.class);
        }
    }

    public static class ListTabTest extends UIBaseTest{
        private MainFxController mainController;

        @Before
        public void setup() {
            MainApp.setUserId(VALID_USER_ID);
            MainApp.setUserEmail(VALID_EMAIL);
        }

        @Override
        public void start(Stage stage) throws Exception {
            var loadNotesPresenter = new LoadNotesPresenter(null) {

                @Override
                public List<NoteDTO> handle(Object ownerId) {
                    return List.of(new NoteDTO(0L, TITLE_ONE, CONTENT_ONE),
                                    new NoteDTO(1L, TITLE_TWO, CONTENT_TWO));
                }
            };
            mainController = setTabAndShow(TAB_HOME, stage);
            mainController.setLoadNotesPresenter(loadNotesPresenter);
        }

        @Test
        public void shouldDeleteNoteWithSuccess() {
            var dropNoteController = new DropNoteController(null) {

                @Override
                public void handle(NoteDTO noteDTO) {
                    LOGGER.info("Note Data: {}", noteDTO);
                    assertThat(noteDTO.getId()).isEqualTo(NOTE_ID);
                    assertThat(noteDTO.getTitle()).isEqualTo(TITLE_ONE);
                }
            };
            mainController.setDropNoteController(dropNoteController);

            clickOn("#tabList").clickOn(TITLE_ONE).clickOn("#btnDrop");

            assertThatThrownBy(() -> {
                getNode(TITLE_ONE, Label.class);
            }).isInstanceOf(EmptyNodeQueryException.class);
        }

        @Test
        public void shouldUpdateNoteWithSuccess() {
            var updateNoteController = new UpdateNoteController(null) {

                @Override
                public void handle(NoteDTO noteDTO) {
                    LOGGER.info("Note Data: {}", noteDTO);
                    assertThat(noteDTO.getId()).isEqualTo(NOTE_ID);
                    assertThat(noteDTO.getTitle()).isEqualTo(TITLE_MOD);
                    assertThat(noteDTO.getContent()).contains(CONTENT_MOD);
                }
            };
            mainController.setUpdateNoteController(updateNoteController);
            clickOn("#tabList").clickOn(TITLE_ONE).clickOn("#btnEdit");
            WaitForAsyncUtils.waitForFxEvents();
            var title =(TextField) getNode("#txtTitle", TextField.class);
            title.clear();

            clickOn("#txtTitle").write(TITLE_MOD);
            clickOn("#htmlContent");
            push(BACK_SPACE).push(BACK_SPACE).push(BACK_SPACE).push(BACK_SPACE)
                    .write(CONTENT_MOD.substring(7));
            clickOn("#btnSave");

            assertThatThrownBy(() -> {
                getNode("Error", Label.class);
            }).isInstanceOf(EmptyNodeQueryException.class);
        }
    }

    private static MainFxController setTabAndShow(int index, Stage stage) throws Exception {
        var fxmlHelper = new FxmlHelper(MAIN_FXML, Locale.getDefault());
        var mainFxController = new MainFxController.Builder().build();
        fxmlHelper.getFxmlLoader().setController(mainFxController);

        var tabPane = (TabPane) fxmlHelper.getFxmlLoader().load();
        tabPane.getSelectionModel().select(index);
        stage.setScene(new Scene(tabPane));
        stage.show();

        return mainFxController;
    }
}
