package net.yoedtos.usecases.ports;

import java.util.List;

public interface NoteRepository {
    NoteData add(NoteData noteData);
    List<NoteData> findAllNotesFrom(Long userId);
    NoteData findById(Long noteId);
    NoteData remove(Long noteId);
    NoteData update(Long noteId, String title, String content);
}
