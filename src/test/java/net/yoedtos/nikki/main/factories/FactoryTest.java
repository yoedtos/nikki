package net.yoedtos.nikki.main.factories;

import static net.yoedtos.nikki.main.factories.Factory.Operation.CREATE;
import static net.yoedtos.nikki.main.factories.Factory.Operation.LOAD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.controllers.CreateNoteController;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import org.junit.Before;
import org.junit.Test;

public class FactoryTest {
    private Factory factory;

    @Before
    public void init() {
        factory = Factory.getInstance();
    }

    @Test
    public void shouldUseOneFactoryWithSuccess() {
        assertThat(Factory.getInstance()).isEqualTo(factory);
    }

    @Test
    public void shouldMakeControllerWithSuccess() {
        var createController = factory.makeController(CREATE);
        assertThat(createController).isInstanceOf(CreateNoteController.class);
    }

    @Test
    public void shouldThrowExceptionWithSuccess() {
        assertThatThrownBy(() -> {
            factory.makeController(LOAD);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessage("Invalid: LOAD");
    }

    @Test
    public void shouldMakePresenterWithSuccess() {
        var loadPresenter = factory.makePresenter(LOAD);
        assertThat(loadPresenter).isInstanceOf(LoadNotesPresenter.class);
    }
}
