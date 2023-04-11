package net.yoedtos.usecases.ports;

import java.util.List;

public interface NoteRepository {
    NoteData addNote(NoteData noteData);
    List<NoteData> findAllNotesFrom(Long userId);
}
