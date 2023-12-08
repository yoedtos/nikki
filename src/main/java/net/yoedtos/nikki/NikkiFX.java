package net.yoedtos.nikki;

import net.yoedtos.nikki.main.MainGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NikkiFX {
    private static final Logger LOGGER = LoggerFactory.getLogger(NikkiFX.class);

    public static void main(String[] args) {
        try {
            MainGUI.begin();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}