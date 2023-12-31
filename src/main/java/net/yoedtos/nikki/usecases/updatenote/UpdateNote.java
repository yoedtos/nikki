package net.yoedtos.nikki.usecases.updatenote;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.entities.Note;
import net.yoedtos.nikki.entities.User;
import net.yoedtos.nikki.entities.error.ExistingTitleError;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;
import net.yoedtos.nikki.usecases.ports.UserRepository;

public class UpdateNote {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public UpdateNote(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Future<Either<Error, NoteData>> perform(Long noteId, NoteData newNote) {
        var user = User.create(this.userRepository.findByEmail(newNote.getOwnerEmail())).get();
        var noteOrError = Note.create(newNote.getTitle(), newNote.getContent(), user);
        if (noteOrError.isLeft()) {
            return Future.of(() -> Either.left(noteOrError.getLeft()));
        }
        var note = noteOrError.get();
        var userNotes = this.noteRepository.findAllNotesFrom(newNote.getOwnerId());
        var optional = userNotes.stream().map(n -> n.getTitle().equals(newNote.getTitle())).findFirst();
        if (optional.get()) {
            return Future.of(() -> Either.left(new ExistingTitleError(newNote.getTitle())));
        }

        return Future.of(() -> Either.right(this.noteRepository.update(noteId, note.getTitle().getValue(), note.getContent())));
    }
}
