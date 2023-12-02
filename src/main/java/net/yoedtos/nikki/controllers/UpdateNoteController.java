package net.yoedtos.nikki.controllers;

import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.updatenote.UpdateNote;

public class UpdateNoteController implements Controller<NoteDTO> {

    private final UpdateNote updateNoteUseCase;

    public UpdateNoteController(UpdateNote useCase) {
        this.updateNoteUseCase = useCase;
    }

    @Override
    public void handle(NoteDTO note) {
        var noteData = new NoteData(note.getId(), MainApp.getUserId(), MainApp.getUserEmail(), note.getTitle(), note.getContent());
        var result = updateNoteUseCase.perform(note.getId(), noteData).get();
        if (result.isLeft()) {
            throw result.getLeft();
        }
    }
}
