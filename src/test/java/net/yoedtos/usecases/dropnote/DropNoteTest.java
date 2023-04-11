package net.yoedtos.usecases.dropnote;

import static net.yoedtos.usecases.TestConstant.VALID_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.usecases.ports.NoteData;
import net.yoedtos.usecases.ports.NoteRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DropNoteTest {
    private Long validUserId = 0L;
    private Long noteId = 0L;
    private final String titleOne = "Note 1";
    private NoteData noteOne;
    private NoteRepository noteRepository;

    @Before
    public void initObject() {
        noteOne =  new NoteData(0L, validUserId, VALID_EMAIL, titleOne, "Content One");
        noteRepository = new InMemoryNoteRepository(new ArrayList<>(List.of(noteOne)));
    }
    @Test
    public void shouldRemoveExistingNote() {
        var dropNoteUseCase = new DropNote(noteRepository);
        var dropped = dropNoteUseCase.perform(noteId).get();
        assertThat(dropped).isEqualTo(noteOne);
        assertThat(noteRepository.findNote(noteId)).isNull();
    }
}
