package net.yoedtos.nikki.usecases.createnote;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.nikki.builders.NoteBuilder;
import net.yoedtos.nikki.builders.UserBuilder;
import net.yoedtos.nikki.entities.error.ExistingTitleError;
import net.yoedtos.nikki.entities.error.InvalidTitleError;
import net.yoedtos.nikki.entities.error.UnregisteredOwnerError;
import net.yoedtos.nikki.usecases.doubles.repositories.InMemoryNoteRepository;
import net.yoedtos.nikki.usecases.doubles.repositories.InMemoryUserRepository;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateNoteTest {
    private UserData validUser;
    private NoteRepository noteRepository;
    private UserRepository userRepository;
    private CreateNote createNoteUseCase;
    private NoteData validCreateNoteRequest;

    @Before
    public void initObject() {
        validUser = UserBuilder.create().build();
        userRepository = new InMemoryUserRepository(new ArrayList<>(List.of(validUser)));
        noteRepository = new InMemoryNoteRepository(new ArrayList<>());
        createNoteUseCase = new CreateNote(noteRepository, userRepository);
        validCreateNoteRequest = NoteBuilder.create().build();
    }

    @Test
    public void shouldCreateNoteWithValidUserAndTitle() {
        var response = createNoteUseCase.perform(validCreateNoteRequest).get();
        var addedNotes = noteRepository.findAllNotesFrom(validUser.getId());
        assertThat(addedNotes.size()).isEqualTo(1);
        assertThat(addedNotes.get(0).getTitle()).isEqualTo(VALID_TITLE);
        assertThat(response.get().getId()).isEqualTo(0);
    }

    @Test
    public void shouldNotCreateNoteWithUnregisteredOwner() {
        var invalidCreateNoteRequest = NoteBuilder.create().withUnregisteredOwner().build();
        var error = createNoteUseCase.perform(invalidCreateNoteRequest).get();
        assertThat(error.getLeft()).isExactlyInstanceOf(UnregisteredOwnerError.class);
        assertThat(error.getLeft().getMessage()).isEqualTo("Owner unregistered.");
    }

    @Test
    public void shouldNotCreateNoteWithInvalidTitle() {
        var invalidCreateNoteRequest = NoteBuilder.create().withInvalidTitle().build();
        var error = createNoteUseCase.perform(invalidCreateNoteRequest).get();
        assertThat(error.getLeft()).isExactlyInstanceOf(InvalidTitleError.class);
        assertThat(error.getLeft().getMessage()).isEqualTo("Invalid title: " + INVALID_TITLE + ".");
    }

    @Test
    public void shouldNotCreateNoteWithExistingTitle() {
        noteRepository.add(validCreateNoteRequest);
        var invalidCreateNoteRequest = NoteBuilder.create().build();
        var error = createNoteUseCase.perform(invalidCreateNoteRequest).get();
        assertThat(error.getLeft()).isExactlyInstanceOf(ExistingTitleError.class);
        assertThat(error.getLeft().getMessage()).isEqualTo("Title " + VALID_TITLE + " already exist.");
    }
}