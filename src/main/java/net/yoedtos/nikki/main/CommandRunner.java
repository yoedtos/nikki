package net.yoedtos.nikki.main;

import static net.yoedtos.nikki.main.CommandRunner.Command.INIT;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteSetUp;
import net.yoedtos.nikki.main.config.ConfigHandler;
import net.yoedtos.nikki.main.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRunner.class);

    class Command {
        public static final String INIT = "-init";
    }

    private NitriteSetUp nitriteSetUp;

    public CommandRunner() throws AppException {
        this.nitriteSetUp = new NitriteSetUp(new ConfigHandler(Constants.CONFIG_FILE));
    }

    public void execute(String[] args) throws AppException {
        switch (args[0]) {
            case INIT:
                if (args.length == 1)
                    showError("argument " + null);

                LOGGER.debug("Run command: {} {}", args[0], args[1]);
                nitriteSetUp.setPassword(args[1]);
                break;
            default:
                showError("command " + args[0]);
        }
    }

    private void showError(String value) throws AppException {
        throw new AppException("Invalid " + value);
    }
}
