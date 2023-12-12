package net.yoedtos.nikki.it;

import static net.yoedtos.nikki.main.factories.Factory.Operation.*;
import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.controllers.*;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteNoteRepository;
import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteException;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.main.factories.Factory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OrderWith(Alphanumeric.class)
public class Controllers extends BaseIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controllers.class);

    private EnterDTO enterDTO;
    private NitriteNoteRepository noteRepository;
    private static Long noteOneId;

    @Before
    public void init() {
        enterDTO = new EnterDTO(VALID_EMAIL, VALID_PASSWORD);
        noteRepository = new NitriteNoteRepository();
    }

    @Test
    public void A_shouldNotSignInUnknownUser() {
        var signInController = (SignInController) Factory.getInstance().makeController(SIGNIN);
        var unknown = new EnterDTO("unknown@abc.com", VALID_PASSWORD);

        assertThatThrownBy(() -> signInController.handle(unknown))
                .isInstanceOf(UserNotFoundError.class)
                .hasMessage("User not found.");

        assertThat(MainApp.getUserId()).isNull();
        assertThat(MainApp.getUserEmail()).isNull();
    }

    @Test
    public void B_shouldSignUpCorrectly() {
        var signUpController = (SignUpController) Factory.getInstance().makeController(SIGNUP);
        signUpController.handle(enterDTO);
        var usedId = MainApp.getUserId();
        var userEmail = MainApp.getUserEmail();
        assertThat(usedId).isNotNull();
        assertThat(userEmail).isNotNull();
        LOGGER.debug("Id: {} - Email: {}", usedId, userEmail);
    }

    @Test
    public void C_shouldSignInCorrectly() {
        resetCredential();
        var signInController = (SignInController) Factory.getInstance().makeController(SIGNIN);
        signInController.handle(enterDTO);
        var usedId = MainApp.getUserId();
        var userEmail = MainApp.getUserEmail();
        assertThat(usedId).isNotNull();
        assertThat(userEmail).isNotNull();
        LOGGER.debug("Id: {} - Email: {}", usedId, userEmail);
    }

    @Test
    public void C_shouldCreateNoteCorrectly() {
        var noteOne = new NoteDTO(null, TITLE_ONE, CONTENT_ONE);
        var createNoteController = (CreateNoteController) Factory.getInstance().makeController(CREATE);
        createNoteController.handle(noteOne);

        var notes = noteRepository.findAllNotesFrom(MainApp.getUserId());
        assertThat(notes).hasSize(1);
        var noteData = notes.get(0);
        assertThat(noteData.getTitle()).isEqualTo(TITLE_ONE);
        assertThat(noteData.getContent()).isEqualTo(CONTENT_ONE);
        noteOneId = noteData.getId();
        LOGGER.debug("Note Id: {}", noteOneId);
    }

    @Test
    public void D_shouldUpdateNoteCorrectly() {
        var updateNoteController = (UpdateNoteController) Factory.getInstance().makeController(UPDATE);
        updateNoteController.handle(new NoteDTO(noteOneId, TITLE_MOD, CONTENT_MOD));

        var noteData = noteRepository.findById(noteOneId);
        assertThat(noteData.getTitle()).isEqualTo(TITLE_MOD);
        assertThat(noteData.getContent()).isEqualTo(CONTENT_MOD);
        LOGGER.debug("Note data: {}", noteData);
    }

    @Test
    public void E_shouldDropNoteCorrectly() {
        var dropNoteController = (DropNoteController) Factory.getInstance().makeController(DROP);
        dropNoteController.handle(new NoteDTO(noteOneId, TITLE_MOD, CONTENT_MOD));

        assertThatThrownBy(() -> noteRepository.findById(noteOneId))
                .isInstanceOf(NitriteException.class);
    }
}
