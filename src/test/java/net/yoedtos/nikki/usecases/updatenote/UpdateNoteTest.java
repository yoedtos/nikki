package net.yoedtos.nikki.usecases.updatenote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.nikki.builders.NoteBuilder;
import net.yoedtos.nikki.builders.UserBuilder;
import net.yoedtos.nikki.usecases.TestConstant;
import net.yoedtos.nikki.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.nikki.usecases.ports.NoteRepository;
import net.yoedtos.nikki.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class UpdateNoteTest {
    private NoteRepository noteRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        noteRepository = new InMemoryNoteRepository(new ArrayList<>(List.of(TestConstant.NOTE_ONE)));
    }

    @Test
    public void shouldUpdateTitleAndContentOfExistingNote() {
        var validUser = UserBuilder.create().build();
        when(userRepository.findByEmail(TestConstant.VALID_EMAIL)).thenReturn(validUser);
        var newNote = NoteBuilder.create().build();
        var updateNoteUseCase = new UpdateNote(noteRepository, userRepository);
        var response = updateNoteUseCase.perform(TestConstant.NOTE_ID, newNote).get();
        var updateNote = response.get();
        assertThat(updateNote.getTitle()).isEqualTo(TestConstant.VALID_TITLE);
        assertThat(updateNote.getContent()).isEqualTo(TestConstant.VALID_CONTENT);
    }
}
