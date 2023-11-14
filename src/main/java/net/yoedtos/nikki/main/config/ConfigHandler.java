package net.yoedtos.nikki.main.config;

import net.yoedtos.nikki.main.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHandler.class);

    private File configFile;

    public ConfigHandler(String configFile) {
        this.configFile = new File(configFile);
    }

    public Properties read() throws AppException {
        var properties = new Properties();

        try (FileInputStream input = new FileInputStream(configFile)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new AppException(e.getMessage());
        }
        return properties;
    }

    public void write(Properties properties) throws AppException {
        try (FileOutputStream output = new FileOutputStream(configFile)) {
            properties.store(output, Constants.CONFIG_FILE_HEADER);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new AppException(e.getMessage());
        }
    }
}
