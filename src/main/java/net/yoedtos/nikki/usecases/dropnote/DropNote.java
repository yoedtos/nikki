package net.yoedtos.nikki.usecases.dropnote;

import io.vavr.concurrent.Future;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;

public class DropNote {
    private final NoteRepository noteRepository;

    public DropNote(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Future<NoteData> perform(Long noteId) {
        return Future.of(() -> {
            var note = this.noteRepository.findById(noteId);
            if (note != null) {
                this.noteRepository.remove(noteId);
            }
            return note;
        });
    }
}
