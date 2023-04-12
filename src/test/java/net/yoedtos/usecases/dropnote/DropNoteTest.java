package net.yoedtos.usecases.dropnote;

import static net.yoedtos.usecases.TestConstant.NOTE_ID;
import static net.yoedtos.usecases.TestConstant.NOTE_ONE;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.usecases.ports.NoteRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DropNoteTest {
    private NoteRepository noteRepository;

    @Before
    public void initObject() {
        noteRepository = new InMemoryNoteRepository(new ArrayList<>(List.of(NOTE_ONE)));
    }
    @Test
    public void shouldRemoveExistingNote() {
        var dropNoteUseCase = new DropNote(noteRepository);
        var dropped = dropNoteUseCase.perform(NOTE_ID).get();
        assertThat(dropped).isEqualTo(NOTE_ONE);
        assertThat(noteRepository.findNote(NOTE_ID)).isNull();
    }
}
