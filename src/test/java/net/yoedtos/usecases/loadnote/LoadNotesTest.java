package net.yoedtos.usecases.loadnote;

import static net.yoedtos.usecases.TestConstant.VALID_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.usecases.createnote.ports.NoteData;
import net.yoedtos.usecases.createnote.ports.NoteRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadNotesTest {
    private NoteRepository noteRepository;
    private Long validUserId = 0L;
    private Long invalidUserId = 1L;
    private final String titleOne = "Note 1";
    private final String titleTwo = "Note 2";

    @Before
    public void initObject() {
        var notesData = new ArrayList<>(Arrays.asList(
                new NoteData(0L, validUserId, VALID_EMAIL, titleOne, "Content One"),
                new NoteData(1L, validUserId, VALID_EMAIL, titleTwo, "Content Two")));

        noteRepository = new InMemoryNoteRepository(notesData);
    }

    @Test
    public void shouldLoadNotesForRegisteredUser() {
        var loadNoteUseCase = new LoadNotes(noteRepository);
        var notes = loadNoteUseCase.perform(validUserId).get();
        assertThat(notes.size()).isEqualTo(2);
        assertThat(notes.get(0).getTitle()).isEqualTo(titleOne);
        assertThat(notes.get(1).getTitle()).isEqualTo(titleTwo);
    }

    @Test
    public void shouldFailLoadNotesForUserWithoutNotes() {
        var loadNoteUseCase = new LoadNotes(noteRepository);
        var notes = loadNoteUseCase.perform(invalidUserId).get();
        assertThat(notes.size()).isEqualTo(0);
    }
}
