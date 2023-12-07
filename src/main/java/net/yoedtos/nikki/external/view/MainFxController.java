package net.yoedtos.nikki.external.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.yoedtos.nikki.controllers.CreateNoteController;
import net.yoedtos.nikki.controllers.DropNoteController;
import net.yoedtos.nikki.controllers.UpdateNoteController;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainFxController extends ViewFx {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFxController.class);

    @FXML private Label lbUser;
    @FXML private Label lbTitle;
    @FXML private TextField txtTitle;
    @FXML private HTMLEditor htmlContent;
    @FXML private TabPane tabPane;
    @FXML private Tab tabHome;
    @FXML private Tab tabCreate;
    @FXML private ListView<NoteDTO> viewNotes;
    @FXML private WebView webNote;

    private Stage stage;
    private NoteDTO noteDTO;
    private List<NoteDTO> listNotes;
    private LoadNotesPresenter loadNotesPresenter;
    private CreateNoteController createNoteController;
    private DropNoteController dropNoteController;
    private UpdateNoteController updateNoteController;

    private MainFxController(Builder builder) {
        this.loadNotesPresenter = builder.loadNotesPresenter;
        this.createNoteController = builder.createNoteController;
        this.dropNoteController = builder.dropNoteController;
        this.updateNoteController = builder.updateNoteController;
    }

    public void setLoadNotesPresenter(LoadNotesPresenter loadNotesPresenter) {
        this.loadNotesPresenter = loadNotesPresenter;
    }

    public void setCreateNoteController(CreateNoteController createNoteController) {
        this.createNoteController = createNoteController;
    }

    public void setDropNoteController(DropNoteController dropNoteController) {
        this.dropNoteController = dropNoteController;
    }

    public void setUpdateNoteController(UpdateNoteController updateNoteController) {
        this.updateNoteController = updateNoteController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.debug("{} init completed", MainFxController.class.getSimpleName());

        viewNotes.setOnMouseClicked(mouseEvent -> {
            noteDTO = viewNotes.getSelectionModel().getSelectedItem();
            lbTitle.setText(noteDTO.getTitle());
            var webEngine = webNote.getEngine();
            webEngine.loadContent(noteDTO.getContent());
        });
    }
    @Override
    public void onStageDefined(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onSelect() {
        lbUser.setText(MainApp.getUserEmail());
        listNotes = new ArrayList<>(loadNotesPresenter.handle(MainApp.getUserId()));
        viewNotes.setItems(FXCollections.observableList(listNotes));
    }

    @FXML
    public void saveNote() {
        try {
            if (noteDTO != null) {
                updateNoteController.handle(
                        new NoteDTO(noteDTO.getId(), txtTitle.getText(), htmlContent.getHtmlText()));
            } else {
                createNoteController.handle(
                        new NoteDTO(null, txtTitle.getText(), htmlContent.getHtmlText()));
            }
            clearFields();
        } catch (Error error) {
            showError(error.getMessage());
        }
    }

    @FXML
    public void dropNote() {
        if (noteDTO != null) {
            try {
                dropNoteController.handle(noteDTO);
                listNotes.remove(noteDTO);
                updateAfterDrop();
            } catch (Error error) {
                showError(error.getMessage());
            }
        }
    }

    @FXML
    public void editNote() {
        tabPane.getSelectionModel().select(tabCreate);
        txtTitle.setText(noteDTO.getTitle());
        htmlContent.setHtmlText(noteDTO.getContent());
    }

    @FXML
    public void clearFields() {
        txtTitle.clear();
        htmlContent.setHtmlText("");
    }

    @FXML
    public void onExit() {
        Platform.exit();
        System.exit(0);
        LOGGER.debug("Exiting Nikki");
    }

    private void updateAfterDrop() {
        viewNotes.refresh();
        webNote.getEngine().loadContent("");
        lbTitle.setText("");
    }

    public static class Builder {
        private LoadNotesPresenter loadNotesPresenter;
        private CreateNoteController createNoteController;
        private DropNoteController dropNoteController;
        private UpdateNoteController updateNoteController;

        public Builder() {}

        public Builder loadNoteController(LoadNotesPresenter loadNotesPresenter) {
            this.loadNotesPresenter = loadNotesPresenter;
            return this;
        }

        public Builder createNoteController(CreateNoteController createNoteController) {
            this.createNoteController = createNoteController;
            return this;
        }

        public Builder dropNoteController(DropNoteController dropNoteController) {
            this.dropNoteController = dropNoteController;
            return this;
        }

        public Builder updateNoteController(UpdateNoteController updateNoteController) {
            this.updateNoteController = updateNoteController;
            return this;
        }

        public MainFxController build() {
            return new MainFxController(this);
        }
    }
}
