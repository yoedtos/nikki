package net.yoedtos.nikki.external.repositories.nitrite.helper;

import static net.yoedtos.nikki.external.repositories.nitrite.helper.Keys.*;

import net.yoedtos.nikki.main.AppException;
import net.yoedtos.nikki.main.config.ConfigHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class NitriteSetUp {
    private static final Logger LOGGER = LoggerFactory.getLogger(NitriteSetUp.class);

    private static final String APP_DB_NAME = "nikki-db";
    private static final String APP_DB_USER = "nikki-user";

    private Properties config;
    private ConfigHandler configHandler;

    public NitriteSetUp(ConfigHandler configHandler) throws AppException {
        this.configHandler = configHandler;
        checkConfig();
    }

    public void setPassword(String password) throws AppException {
        config = new Properties();
        config.put(DB_NAME, APP_DB_NAME);
        config.put(DB_USER, APP_DB_USER);
        config.put(DB_PASS, password);
        try {
            configHandler.write(config);
        } catch (AppException e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    private void checkConfig() throws AppException {
        config = configHandler.read();
        if (!config.isEmpty()) {
            throw new AppException("There is a configuration already");
        }
    }
}
