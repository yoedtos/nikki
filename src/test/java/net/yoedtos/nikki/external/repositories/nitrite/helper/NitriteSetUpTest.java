package net.yoedtos.nikki.external.repositories.nitrite.helper;

import static net.yoedtos.nikki.external.repositories.nitrite.helper.Keys.*;
import static net.yoedtos.nikki.usecases.TestConstant.CLEAR_PASS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import net.yoedtos.nikki.main.AppException;
import net.yoedtos.nikki.main.config.ConfigHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class NitriteSetUpTest {

    private ConfigHandler configHandler = mock(ConfigHandler.class);
    private NitriteSetUp nitriteSetUp;
    private Properties config;

    @Before
    public void init() {
        config = new Properties();
        config.put(DB_NAME, "nikki-db");
        config.put(DB_USER, "nikki-user");
        config.put(DB_PASS, CLEAR_PASS);
    }

    @Test
    public void shouldCreateConfigurationWithSuccess() throws AppException {
        when(configHandler.read()).thenReturn(new Properties());
        doNothing().when(configHandler).write(any());
        nitriteSetUp = new NitriteSetUp(configHandler);
        nitriteSetUp.setPassword(CLEAR_PASS);
        verify(configHandler, times(1)).write(config);
    }

    @Test
    public void shouldThrowAppExceptionWhenHasConfiguration() throws AppException {
        when(configHandler.read()).thenReturn(config);
        assertThatThrownBy(() -> new NitriteSetUp(configHandler))
                .isInstanceOf(AppException.class)
                .hasMessage("There is a configuration already");

    }
}
