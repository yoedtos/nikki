package net.yoedtos.nikki.usecases.createnote;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.entities.Note;
import net.yoedtos.nikki.entities.User;
import net.yoedtos.nikki.entities.error.ExistingTitleError;
import net.yoedtos.nikki.entities.error.InvalidTitleError;
import net.yoedtos.nikki.entities.error.UnregisteredOwnerError;
import net.yoedtos.nikki.usecases.ports.NoteData;
import net.yoedtos.nikki.usecases.ports.NoteRepository;
import net.yoedtos.nikki.usecases.ports.UserRepository;

public class CreateNote {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public CreateNote(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Future<Either<Error, NoteData>> perform(NoteData request) {
        var owner = this.userRepository.findByEmail(request.getOwnerEmail());
        if (owner == null) {
            return Future.of(() -> Either.left(new UnregisteredOwnerError()));
        }
        var ownerNotes = this.noteRepository.findAllNotesFrom(request.getOwnerId());
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
        return Future.of(() -> Either.right(this.noteRepository.add(noteData)));
    }
}
