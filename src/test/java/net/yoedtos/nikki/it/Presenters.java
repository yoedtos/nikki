package net.yoedtos.nikki.it;

import static net.yoedtos.nikki.main.factories.Factory.Operation.LOAD;
import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteNoteRepository;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.main.factories.Factory;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import net.yoedtos.nikki.usecases.ports.NoteData;
import org.junit.Before;
import org.junit.Test;

public class Presenters extends BaseIT {

    private NitriteNoteRepository noteRepository;
    private Long userId;

    @Before
    public void init() {
        userId = MainApp.getUserId();
        var userEmail = MainApp.getUserEmail();
        noteRepository = new NitriteNoteRepository();
        noteRepository.add(new NoteData
                (null, userId, userEmail, TITLE_ONE, CONTENT_ONE));
        noteRepository.add(new NoteData
                (null, userId, userEmail, TITLE_TWO, CONTENT_TWO));
    }

    @Test
    public void shouldLoadNotesCorrectly() {
        var loadNotesController = (LoadNotesPresenter) Factory.getInstance().makePresenter(LOAD);
        var notes = loadNotesController.handle(userId);
        assertThat(notes)
                .hasSize(2)
                .extracting(NoteDTO::getTitle)
                .contains(TITLE_ONE, TITLE_TWO);
    }
}
