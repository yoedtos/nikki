package net.yoedtos.usecases.createnote;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.entities.UserData;
import net.yoedtos.usecases.createnote.ports.NoteData;
import net.yoedtos.usecases.createnote.ports.NoteRepository;
import net.yoedtos.usecases.signup.InMemoryUserRepository;
import net.yoedtos.usecases.signup.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CreateNoteTest {
    private final String emptyContent = "";
    private UserData validRegisteredUser;
    private NoteRepository noteRepository;
    private UserRepository userRepository;

    @Before
    public void initObject() {
        validRegisteredUser = new UserData(0L, VALID_EMAIL, VALID_PASSWORD);
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        userRepository.addUser(validRegisteredUser);
        noteRepository = new InMemoryNoteRepository(new ArrayList<>());
    }

    @Test
    public void shouldCreateNoteWithValidUserAndTitle() {
        var validCreateNoteRequest = new NoteData(null, validRegisteredUser.getId(), validRegisteredUser.getEmail(), VALID_TITLE, emptyContent);
        var createNoteUseCase = new CreateNote(noteRepository, userRepository);
        var response = createNoteUseCase.perform(validCreateNoteRequest).get();
        var addedNotes = noteRepository.findAllNotesFrom(validRegisteredUser.getId()).get();
        assertThat(addedNotes.size()).isEqualTo(1);
        assertThat(addedNotes.get(0).getTitle()).isEqualTo(VALID_TITLE);
        assertThat(response.getId()).isEqualTo(0);
    }
}
