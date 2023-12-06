package net.yoedtos.nikki.external.view;

import static net.yoedtos.nikki.main.config.Constants.RSRC_BUNDLE;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FxmlHelper {

    private final FXMLLoader fxmlLoader;

    public FxmlHelper(String fxmlView, Locale locale) {
        var resource = ResourceBundle.getBundle(RSRC_BUNDLE, locale);
        this.fxmlLoader = new FXMLLoader(FxmlHelper.class.getResource(fxmlView), resource);
    }

    public FXMLLoader getFxmlLoader() throws Exception {
        if (fxmlLoader != null) {
            return fxmlLoader;
        }
        throw new Exception("Initialization failed");
    }

    public Scene createScene() throws IOException {
        return new Scene(fxmlLoader.load());
    }
}
