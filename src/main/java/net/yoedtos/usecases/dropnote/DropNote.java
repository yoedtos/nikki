package net.yoedtos.usecases.dropnote;

import io.vavr.concurrent.Future;
import net.yoedtos.usecases.ports.NoteData;
import net.yoedtos.usecases.ports.NoteRepository;

public class DropNote {
    private final NoteRepository noteRepository;

    public DropNote(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Future<NoteData> perform(Long noteId) {
        return Future.of(() -> {
            var note = this.noteRepository.findNote(noteId);
            if (note != null) {
                this.noteRepository.remove(noteId);
            }
            return note;
        });
    }
}
