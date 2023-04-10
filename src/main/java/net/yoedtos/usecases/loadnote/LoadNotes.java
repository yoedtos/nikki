package net.yoedtos.usecases.loadnote;

import io.vavr.concurrent.Future;
import net.yoedtos.usecases.createnote.ports.NoteData;
import net.yoedtos.usecases.createnote.ports.NoteRepository;

import java.util.List;

public class LoadNotes {
    private final NoteRepository noteRepository;

    public LoadNotes(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Future<List<NoteData>> perform(Long validUserId) {
        return Future.of(() -> this.noteRepository.findAllNotesFrom(validUserId)).get();
    }
}
