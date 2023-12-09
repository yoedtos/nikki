package net.yoedtos.nikki.main;

import static net.yoedtos.nikki.main.CommandRunner.Command.INIT;
import static net.yoedtos.nikki.usecases.TestConstant.CLEAR_PASS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteSetUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandRunnerTest {
    private static final String INVALID_COMMAND = "-ini";

    @Mock
    private NitriteSetUp nitriteSetUp;

    @InjectMocks
    private CommandRunner commandRunner;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldExecuteCommandWithSuccess() throws AppException {
        var args = new String[]{ INIT, CLEAR_PASS };
        commandRunner.execute(args);
        verify(nitriteSetUp, times(1)).setPassword(CLEAR_PASS);
    }

    @Test
    public void shouldThrowExceptionWithInvalidCommand() {
        var args = new String[]{ INVALID_COMMAND, CLEAR_PASS };
        assertThatThrownBy(() -> commandRunner.execute(args))
                .isInstanceOf(AppException.class)
                .hasMessage("Invalid command " + INVALID_COMMAND);
    }

    @Test
    public void shouldThrowExceptionWithMissParameter() {
        var args = new String[]{ INIT, };
        assertThatThrownBy(() -> commandRunner.execute(args))
                .isInstanceOf(AppException.class)
                .hasMessage("Invalid argument null");
    }
}
