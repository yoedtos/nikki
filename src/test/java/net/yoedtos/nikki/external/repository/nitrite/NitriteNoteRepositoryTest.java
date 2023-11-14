package net.yoedtos.nikki.external.repository.nitrite;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteException;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteNoteRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;

@OrderWith(Alphanumeric.class)
public class NitriteNoteRepositoryTest {

    private static NitriteNoteRepository noteRepository;
    private static long noteOneId;

    @BeforeClass
    public static void begin() {
        noteRepository = new NitriteNoteRepository();
    }

    @Test
    public void A_shouldAddValidNote() {
        var addedNote = noteRepository.add(NOTE_ONE);
        assertThat(addedNote.getId()).isNotNull();
        noteOneId = addedNote.getId();
        var note = noteRepository.findById(addedNote.getId());
        assertThat(note).isNotNull();
    }

    @Test
    public void B_shouldFindAllNotesFromUser() {
        noteRepository.add(NOTE_TWO);
        var notes = noteRepository.findAllNotesFrom(VALID_USER_ID);
        assertThat(notes).hasSize(2);
    }

    @Test
    public void C_shouldUpdateTitleOfExistingNote() {
        noteRepository.update(noteOneId, TITLE_MOD, VALID_CONTENT);
        var editNote = noteRepository.findById(noteOneId);
        assertThat(editNote.getTitle()).isEqualTo(TITLE_MOD);
        assertThat(editNote.getContent()).isEqualTo(VALID_CONTENT);
    }

    @Test
    public void D_shouldUpdateContentOfExistingNote() {
        noteRepository.update(noteOneId, VALID_TITLE, CONTENT_MOD);
        var editNote = noteRepository.findById(noteOneId);
        assertThat(editNote.getTitle()).isEqualTo(VALID_TITLE);
        assertThat(editNote.getContent()).isEqualTo(CONTENT_MOD);
    }

    @Test
    public void E_shouldRemoveExistingNote() {
        noteRepository.remove(noteOneId);
        assertThatThrownBy(() -> {
            noteRepository.findById(noteOneId);
        }).isInstanceOf(NitriteException.class);
    }
}
