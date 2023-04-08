package net.yoedtos.usecases.createnote;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.entities.UserData;
import net.yoedtos.entities.error.UnregisteredOwnerError;
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
    private CreateNote createNoteUseCase;

    @Before
    public void initObject() {
        validRegisteredUser = new UserData(0L, VALID_EMAIL, VALID_PASSWORD);
        userRepository = new InMemoryUserRepository(new ArrayList<>());
        userRepository.addUser(validRegisteredUser);
        noteRepository = new InMemoryNoteRepository(new ArrayList<>());
        createNoteUseCase = new CreateNote(noteRepository, userRepository);
    }

    @Test
    public void shouldCreateNoteWithValidUserAndTitle() {
        var validCreateNoteRequest = new NoteData(null, validRegisteredUser.getId(), validRegisteredUser.getEmail(), VALID_TITLE, emptyContent);
        var response = createNoteUseCase.perform(validCreateNoteRequest).get();
        var addedNotes = noteRepository.findAllNotesFrom(validRegisteredUser.getId()).get();
        assertThat(addedNotes.size()).isEqualTo(1);
        assertThat(addedNotes.get(0).getTitle()).isEqualTo(VALID_TITLE);
        assertThat(response.get().getId()).isEqualTo(0);
    }

    @Test
    public void shouldNotCreateNoteWithUnregisteredOwner() {
        var unRegisteredOwner = new UserData(0L, "unknwon@mail.com", VALID_PASSWORD);
        var inValidCreateNoteRequest = new NoteData(null, unRegisteredOwner.getId(), unRegisteredOwner.getEmail(), VALID_TITLE, emptyContent);
        var error = createNoteUseCase.perform(inValidCreateNoteRequest).get();
        assertThat(error.getLeft()).isExactlyInstanceOf(UnregisteredOwnerError.class);
        assertThat(error.getLeft().getMessage()).isEqualTo("Owner unregistered.");
    }
}