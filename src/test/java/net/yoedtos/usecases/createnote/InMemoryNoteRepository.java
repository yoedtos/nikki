package net.yoedtos.usecases.createnote;

import io.vavr.concurrent.Future;
import net.yoedtos.usecases.createnote.ports.NoteData;
import net.yoedtos.usecases.createnote.ports.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryNoteRepository implements NoteRepository {
    private List<NoteData> notesData;

    public InMemoryNoteRepository(ArrayList<NoteData> notesArray) {
        this.notesData = notesArray;
    }

    @Override
    public Future<NoteData> addNote(NoteData noteData) {
        var size = notesData.size();
        var note = new NoteData(Long.valueOf(size), noteData.getOwnerId(), noteData.getOwnerEmail(), noteData.getTitle(), noteData.getContent());
        notesData.add(note);
        return Future.of(() -> notesData.get(size));
    }

    @Override
    public Future<List<NoteData>> findAllNotesFrom(Long userId) {
        return Future.of(() -> notesData.stream()
                .filter(note -> note.getOwnerId().equals(userId))
                .collect(Collectors.toList()));
    }
}
