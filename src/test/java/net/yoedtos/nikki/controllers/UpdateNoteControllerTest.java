package net.yoedtos.nikki.controllers;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.entities.error.ExistingTitleError;
import net.yoedtos.nikki.entities.error.InvalidTitleError;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.updatenote.UpdateNote;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UpdateNoteControllerTest {
    @Mock
    private UpdateNote updateNoteUseCase;

    @InjectMocks
    private UpdateNoteController controller;

    private NoteDTO noteDTO;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        noteDTO = new NoteDTO(NOTE_ID, TITLE_MOD, CONTENT_MOD);
        MainApp.setUserId(VALID_USER_ID);
        MainApp.setUserEmail(VALID_EMAIL);
    }

    @Test
    public void shouldUpdateNoteWithSuccess() {
        var noteMod = new NoteData(NOTE_ID, VALID_USER_ID, VALID_EMAIL, TITLE_MOD, CONTENT_MOD);
        when(updateNoteUseCase.perform(any(), any()))
                .thenReturn(Future.of(() -> Either.right(noteMod)));
        controller.handle(noteDTO);
        verify(updateNoteUseCase, times(1)).perform(NOTE_ID, noteMod);
    }

    @Test
    public void shouldNotUpdateNoteWithInvalidTitle() {
        when(updateNoteUseCase.perform(any(), any()))
                .thenReturn(Future.of(() -> Either.left(new InvalidTitleError(INVALID_TITLE))));
        assertThatThrownBy(() -> controller.handle(noteDTO)).isInstanceOf(InvalidTitleError.class);
    }

    @Test
    public void shouldNotUpdateNoteWithSameTitle() {
        when(updateNoteUseCase.perform(any(), any()))
                .thenReturn(Future.of(() -> Either.left(new ExistingTitleError(TITLE_MOD))));
        assertThatThrownBy(() -> controller.handle(noteDTO)).isInstanceOf(ExistingTitleError.class);
    }
}
