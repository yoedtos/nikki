package net.yoedtos.usecases.createnote;

import io.vavr.concurrent.Future;
import net.yoedtos.entities.Note;
import net.yoedtos.entities.User;
import net.yoedtos.usecases.createnote.ports.NoteData;
import net.yoedtos.usecases.createnote.ports.NoteRepository;
import net.yoedtos.usecases.signup.ports.UserRepository;

public class CreateNote {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public CreateNote(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Future<NoteData> perform(NoteData request) {
        var owner = this.userRepository.findUserByEmail(request.getOwnerEmail()).get();
        var note = Note.create(request.getTitle(), request.getContent(), User.create(owner).get()).get();
        var noteData = new NoteData(null, owner.getId(), owner.getEmail(), note.getTitle().getValue(), note.getContent());
        return Future.of(() -> this.noteRepository.addNote(noteData).get());
    }
}
