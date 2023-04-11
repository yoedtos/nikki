package net.yoedtos.usecases.ports;

import io.vavr.concurrent.Future;

import java.util.List;

public interface NoteRepository {
    Future<NoteData> addNote(NoteData noteData);
    Future<List<NoteData>> findAllNotesFrom(Long userId);
}
