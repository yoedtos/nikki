package net.yoedtos.nikki.it;

import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.main.config.Constants;

import java.io.File;

public abstract class BaseIT {
    protected static File configFile = new File(Constants.CONFIG_FILE);
    protected static File dbFile = new File("nikki-db");


    protected void resetCredential() {
        MainApp.setUserId(null);
        MainApp.setUserEmail(null);
    }
}
