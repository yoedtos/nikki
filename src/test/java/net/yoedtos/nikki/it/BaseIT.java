package net.yoedtos.nikki.it;

import net.yoedtos.nikki.main.config.Constants;

import java.io.File;

public abstract class BaseIT {
    protected static File configFile = new File(Constants.CONFIG_FILE);
    protected static File dbFile = new File("nikki-db");
}
