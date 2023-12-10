package net.yoedtos.nikki.it;

import static net.yoedtos.nikki.usecases.TestConstant.CLEAR_PASS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.main.AppException;
import net.yoedtos.nikki.main.CommandRunner;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;

@OrderWith(Alphanumeric.class)
public class CommandLine extends BaseIT {
    private static final String INIT_COMMAND = "-init";

    @Test
    public void A_shouldMakeInitSetupWithSuccess() throws AppException {
        var args = new String[]{ INIT_COMMAND, CLEAR_PASS };
        new CommandRunner().execute(args);
        assertThat(configFile).exists();
    }

    @Test
    public void B_shouldFailInitSetupIfExists() {
        assertThatThrownBy(() -> new CommandRunner())
                .isInstanceOf(AppException.class)
                .hasMessage("There is a configuration already");
    }
}
