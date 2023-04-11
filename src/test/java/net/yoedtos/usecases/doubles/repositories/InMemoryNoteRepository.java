package net.yoedtos.usecases.doubles.repositories;

import net.yoedtos.usecases.ports.NoteData;
import net.yoedtos.usecases.ports.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryNoteRepository implements NoteRepository {
    private List<NoteData> notesData;

    public InMemoryNoteRepository(ArrayList<NoteData> notesArray) {
        this.notesData = notesArray;
    }

    @Override
    public NoteData addNote(NoteData noteData) {
        var size = notesData.size();
        var note = new NoteData(Long.valueOf(size), noteData.getOwnerId(), noteData.getOwnerEmail(), noteData.getTitle(), noteData.getContent());
        notesData.add(note);
        return notesData.get(size);
    }

    @Override
    public List<NoteData> findAllNotesFrom(Long userId) {
        return notesData.stream()
                .filter(note -> note.getOwnerId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public NoteData findNote(Long noteId) {
        var optional = this.notesData.stream()
                .filter(n -> n.getId() == noteId)
                .findFirst();
        return optional.orElse(null);
    }

    @Override
    public NoteData remove(Long noteId) {
        var note = findNote(noteId);
        if (note != null) {
            this.notesData.remove(note);
            return note;
        }
        return null;
    }
}
