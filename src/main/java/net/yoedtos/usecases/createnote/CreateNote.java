package net.yoedtos.usecases.createnote;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.entities.Note;
import net.yoedtos.entities.User;
import net.yoedtos.entities.error.ExistingTitleError;
import net.yoedtos.entities.error.InvalidTitleError;
import net.yoedtos.entities.error.UnregisteredOwnerError;
import net.yoedtos.usecases.ports.NoteData;
import net.yoedtos.usecases.ports.NoteRepository;
import net.yoedtos.usecases.ports.UserRepository;

public class CreateNote {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public CreateNote(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Future<Either<Error, NoteData>> perform(NoteData request) {
        var owner = this.userRepository.findUserByEmail(request.getOwnerEmail()).get();
        if (owner == null) {
            return Future.of(() -> Either.left(new UnregisteredOwnerError()));
        }
        var ownerNotes = this.noteRepository.findAllNotesFrom(request.getOwnerId()).get();
        var size = ownerNotes.stream()
                .filter(note -> note.getTitle().equals(request.getTitle()))
                .count();
        if ( size > 0) {
            return Future.of(() -> Either.left(new ExistingTitleError(request.getTitle())));
        }
        var noteOrError = Note.create(request.getTitle(), request.getContent(), User.create(owner).get());
        if (noteOrError.isLeft()) {
            return Future.of(() -> Either.left(new InvalidTitleError(request.getTitle())));
        }
        var note = noteOrError.get();
        var noteData = new NoteData(null, owner.getId(), owner.getEmail(), note.getTitle().getValue(), note.getContent());
        return Future.of(() -> Either.right(this.noteRepository.addNote(noteData).get()));
    }
}
