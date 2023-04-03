package net.yoedtos.entities;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.control.Either;
import net.yoedtos.entities.error.InvalidTitleError;
import org.junit.Before;
import org.junit.Test;

public class NoteTest {

    private User validOwner;
    private String validEmail;
    private String validContent;

    @Before
    public void initObject() {
        validEmail = "my@mail.com";
        validOwner = User.create(new UserData(validEmail)).get();
        validContent = "Content of my note";
    }

    @Test
    public void shouldBeCreatedWithValidTitleAndOwner() {
        var validTitle = "My note";
        var note = Note.create(validTitle, validContent, validOwner).get();
        assertThat(note.getTitle().value).isEqualTo(validTitle);
        assertThat(note.getContent()).isEqualTo(validContent);
        assertThat(note.getOwner().email.value).isEqualTo(validEmail);
    }

    @Test
    public void shouldNotBeCreatedWithInvalidTitle() {
        var invalidTitle = "";
        var note = Note.create(invalidTitle, validContent, validOwner);
        assertThat(note).isEqualTo(Either.left(new InvalidTitleError()));
    }
}
