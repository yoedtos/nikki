package net.yoedtos.nikki.presenters;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.vavr.concurrent.Future;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.usecases.loadnote.LoadNotes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class LoadNotesPresenterTest {
    @Mock
    private LoadNotes loadNotesUseCase;

    @InjectMocks
    private LoadNotesPresenter controller;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldLoadNotesWithTwoWithSuccess() {
        when(loadNotesUseCase.perform(any())).thenReturn(Future.of(() -> List.of(NOTE_ONE, NOTE_TWO)));
        var notesResult =  controller.handle(VALID_USER_ID);

        var expected =  List.of(
                new NoteDTO(0L, TITLE_ONE, CONTENT_ONE),
                new NoteDTO(1L, TITLE_TWO, CONTENT_TWO));

        assertThat(notesResult).hasSize(2).isEqualTo(expected);
        verify(loadNotesUseCase, times(1)).perform(VALID_USER_ID);
    }
    @Test
    public void shouldLoadNotesWithoutNotesWithSuccess() {
        when(loadNotesUseCase.perform(any())).thenReturn(Future.of(() -> List.of()));
        var notesResult =  controller.handle(VALID_USER_ID);
        assertThat(notesResult).isEmpty();
        verify(loadNotesUseCase, times(1)).perform(VALID_USER_ID);
    }

    @Test
    public void shouldNotLoadNotesWithInvalidOwner() {
        when(loadNotesUseCase.perform(any())).thenThrow(UserNotFoundError.class);
        assertThatThrownBy(() -> controller.handle(NG_USER_ID))
                .isInstanceOf(UserNotFoundError.class);
    }
}
