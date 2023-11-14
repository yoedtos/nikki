package net.yoedtos.nikki.external.repositories.nitrite.helper;

import static net.yoedtos.nikki.external.repositories.nitrite.helper.Keys.*;

import net.yoedtos.nikki.main.config.ConfigHandler;
import net.yoedtos.nikki.main.config.Constants;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class NitriteHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(NitriteHelper.class);

    private static final String DATABASE = "";
    private static final String USERNAME = "user_nikki";
    private static final String PASSWORD = "pin_nikki";

    private Nitrite nitrite;

    public NitriteHelper() {
        Properties config;
        try {
            config = new ConfigHandler(Constants.CONFIG_FILE).read();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
        this.nitrite = Nitrite.builder()
                .compressed()
                .filePath((String) config.getOrDefault(DB_NAME, DATABASE))
                .openOrCreate((String) config.getOrDefault(DB_USER, USERNAME),
                             (String) config.getOrDefault(DB_PASS, PASSWORD));
    }

    public ObjectRepository getRepository(Class clazz) {
        return nitrite.getRepository(clazz);
    }

    public void close() {
        if (!nitrite.isClosed()) {
            nitrite.close();
        }
    }
}
