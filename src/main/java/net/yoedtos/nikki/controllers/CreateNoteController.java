package net.yoedtos.nikki.controllers;

import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.createnote.CreateNote;
import net.yoedtos.nikki.usecases.ports.NoteData;

public class CreateNoteController implements Controller<NoteDTO> {

    private final CreateNote createNoteUserCase;

    public CreateNoteController(CreateNote useCase) {
        this.createNoteUserCase = useCase;
    }

    @Override
    public void handle(NoteDTO note) {
        var noteData = new NoteData(null, MainApp.getUserId(), MainApp.getUserEmail(), note.getTitle(), note.getContent());
        var result = createNoteUserCase.perform(noteData).get();
        if (result.isLeft()) {
            throw result.getLeft();
        }
    }
}
