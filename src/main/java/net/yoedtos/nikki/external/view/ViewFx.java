package net.yoedtos.nikki.external.view;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class ViewFx implements Initializable {
    public abstract void onStageDefined(Stage stage);

    protected void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).show();
    }

    protected void showWarning(String message) {
        new Alert(Alert.AlertType.WARNING, message).show();
    }
}
