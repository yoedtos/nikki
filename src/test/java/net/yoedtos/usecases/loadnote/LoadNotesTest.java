package net.yoedtos.usecases.loadnote;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.usecases.ports.NoteRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadNotesTest {
    private NoteRepository noteRepository;
    private Long invalidUserId = 1L;

    @Before
    public void initObject() {
        var notesData = new ArrayList<>(Arrays.asList(NOTE_ONE, NOTE_TWO));
        noteRepository = new InMemoryNoteRepository(notesData);
    }

    @Test
    public void shouldLoadNotesForRegisteredUser() {
        var loadNoteUseCase = new LoadNotes(noteRepository);
        var notes = loadNoteUseCase.perform(VALID_USER_ID).get();
        assertThat(notes.size()).isEqualTo(2);
        assertThat(notes.get(0).getTitle()).isEqualTo(TITLE_ONE);
        assertThat(notes.get(1).getTitle()).isEqualTo(TITLE_TWO);
    }

    @Test
    public void shouldFailLoadNotesForUserWithoutNotes() {
        var loadNoteUseCase = new LoadNotes(noteRepository);
        var notes = loadNoteUseCase.perform(invalidUserId).get();
        assertThat(notes.size()).isEqualTo(0);
    }
}
