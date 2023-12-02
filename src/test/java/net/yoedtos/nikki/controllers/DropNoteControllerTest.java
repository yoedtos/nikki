package net.yoedtos.nikki.controllers;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.vavr.concurrent.Future;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.usecases.dropnote.DropNote;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DropNoteControllerTest {
    @Mock
    private DropNote dropNoteUseCase;

    @InjectMocks
    private DropNoteController controller;

    private NoteDTO noteDTO;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        noteDTO = new NoteDTO(NOTE_ID, TITLE_ONE, VALID_CONTENT);
    }

    @Test
    public void shouldDropNoteWithSuccess() {
        var noteOne = NOTE_ONE;
        when(dropNoteUseCase.perform(any())).thenReturn(Future.of(() -> noteOne));
        controller.handle(noteDTO);
        verify(dropNoteUseCase, times(1)).perform(NOTE_ID);
    }

    @Test
    public void shouldThrowErrorWhenDropNoteFail() {
        when(dropNoteUseCase.perform(any())).thenReturn(Future.of(() -> null));
        assertThatThrownBy(() -> controller.handle(noteDTO))
                .isInstanceOf(Error.class)
                .withFailMessage("Failed to remove note " + NOTE_ID);
    }
}
