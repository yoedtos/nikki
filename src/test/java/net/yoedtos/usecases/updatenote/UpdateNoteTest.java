package net.yoedtos.usecases.updatenote;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import net.yoedtos.builders.UserBuilder;
import net.yoedtos.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.usecases.ports.NoteData;
import net.yoedtos.usecases.ports.NoteRepository;
import net.yoedtos.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class UpdateNoteTest {
    private String newTitle = "Title changed";
    private String newContent = "Content One Changed";
    private NoteData newNote;
    private NoteRepository noteRepository;
    @Mock
    private UserRepository userRepository;

    @Before
    public void initObjects() {
        MockitoAnnotations.openMocks(this);
        newNote = new NoteData(NOTE_ID, VALID_USER_ID, VALID_EMAIL, newTitle, newContent);
        noteRepository = new InMemoryNoteRepository(new ArrayList<>(List.of(NOTE_ONE)));
    }

    @Test
    public void shouldUpdateTitleAndContentOfExistingNote() {
        var validUser = UserBuilder.create().build();
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(validUser);

        var updateNoteUseCase = new UpdateNote(noteRepository, userRepository);
        var response = updateNoteUseCase.perform(NOTE_ID, newNote).get();
        var updateNote = response.get();
        assertThat(updateNote.getTitle()).isEqualTo(newTitle);
        assertThat(updateNote.getContent()).isEqualTo(newContent);
    }
}
