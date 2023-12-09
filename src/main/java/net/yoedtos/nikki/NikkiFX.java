package net.yoedtos.nikki;

import net.yoedtos.nikki.main.CommandRunner;
import net.yoedtos.nikki.main.MainGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class NikkiFX {
    private static final Logger LOGGER = LoggerFactory.getLogger(NikkiFX.class);

    public static void main(String[] args) {
        try {
            if(args.length == 0)
                MainGUI.begin();
            else {
                LOGGER.debug("Args: {}", Arrays.toString(args));
                new CommandRunner().execute(args);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}