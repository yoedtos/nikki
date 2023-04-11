package net.yoedtos.entities;

import static net.yoedtos.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.control.Either;
import net.yoedtos.entities.error.InvalidTitleError;
import net.yoedtos.usecases.ports.UserData;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

    private User validOwner;

    @Before
    public void initObject() {
        validOwner = User.create(new UserData(null, VALID_EMAIL, VALID_PASSWORD)).get();
    }

    @Test
    public void shouldBeCreatedWithValidTitleAndOwner() {
        var note = Note.create(VALID_TITLE, VALID_CONTENT, validOwner).get();
        assertThat(note.getTitle().getValue()).isEqualTo(VALID_TITLE);
        assertThat(note.getContent()).isEqualTo(VALID_CONTENT);
        assertThat(note.getOwner().getEmail().getValue()).isEqualTo(VALID_EMAIL);
    }

    @Test
    public void shouldNotBeCreatedWithInvalidTitle() {
        var invalidTitle = "";
        var note = Note.create(invalidTitle, VALID_CONTENT, validOwner);
        assertThat(note).isEqualTo(Either.left(new InvalidTitleError(invalidTitle)));
    }
}
