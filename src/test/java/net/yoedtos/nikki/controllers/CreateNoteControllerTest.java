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
import net.yoedtos.nikki.usecases.createnote.CreateNote;
import net.yoedtos.nikki.usecases.ports.NoteData;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreateNoteControllerTest {
    @Mock
    private CreateNote createNoteUseCase;

    @InjectMocks
    private CreateNoteController controller;

    private NoteDTO noteDTO;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        noteDTO = new NoteDTO(null, TITLE_ONE, VALID_CONTENT);
        MainApp.setUserId(VALID_USER_ID);
        MainApp.setUserEmail(VALID_EMAIL);
    }

    @Test
    public void shouldCreateNoteWithSuccess() {
        var noteOne = new NoteData(null, VALID_USER_ID, VALID_EMAIL, TITLE_ONE, VALID_CONTENT);
        when(createNoteUseCase.perform(any())).thenReturn(Future.of(() -> Either.right(noteOne)));
        controller.handle(noteDTO);
        verify(createNoteUseCase, times(1)).perform(noteOne);
    }

    @Test
    public void shouldNotCreateNoteWithInvalidTitle() {
        when(createNoteUseCase.perform(any())).thenReturn(Future.of(() -> Either.left(new InvalidTitleError(INVALID_TITLE))));
        assertThatThrownBy(() -> {
            controller.handle(noteDTO);
        }).isInstanceOf(InvalidTitleError.class);
    }

    @Test
    public void shouldNotCreateNoteWithSameTitle() {
        when(createNoteUseCase.perform(any())).thenReturn(Future.of(() -> Either.left(new ExistingTitleError(TITLE_ONE))));
        assertThatThrownBy(() -> controller.handle(noteDTO)).isInstanceOf(ExistingTitleError.class);
    }
}
