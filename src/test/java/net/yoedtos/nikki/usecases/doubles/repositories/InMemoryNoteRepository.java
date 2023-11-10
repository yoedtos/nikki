package net.yoedtos.nikki.usecases.doubles.repositories;

import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryNoteRepository implements NoteRepository {
    private List<NoteData> notesData;

    public InMemoryNoteRepository(ArrayList<NoteData> notesArray) {
        this.notesData = notesArray;
    }

    @Override
    public NoteData add(NoteData noteData) {
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
    public NoteData findById(Long noteId) {
        var optional = this.notesData.stream()
                .filter(n -> n.getId() == noteId)
                .findFirst();
        return optional.orElse(null);
    }

    @Override
    public NoteData remove(Long noteId) {
        var note = findById(noteId);
        if (note != null) {
            this.notesData.remove(note);
            return note;
        }
        return null;
    }

    @Override
    public NoteData update(Long noteId, String title, String content) {
        var noteData = this.findById(noteId);
        return new NoteData(noteId, noteData.getOwnerId(), noteData.getOwnerEmail(), title, content);
    }
}
