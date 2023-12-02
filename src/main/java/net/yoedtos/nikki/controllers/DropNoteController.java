package net.yoedtos.nikki.controllers;

import net.yoedtos.nikki.controllers.ports.NoteDTO;
import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.usecases.dropnote.DropNote;

public class DropNoteController implements Controller<NoteDTO> {

    private final DropNote dropNoteUseCase;

    public DropNoteController(DropNote useCase) {
        this.dropNoteUseCase = useCase;
    }

    @Override
    public void handle(NoteDTO note) {
        var result = dropNoteUseCase.perform(note.getId()).get();
        if (result == null) {
            throw new Error("Failed to remove note " + note.getId());
        }
    }
}
