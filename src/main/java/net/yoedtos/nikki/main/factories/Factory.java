package net.yoedtos.nikki.main.factories;

import net.yoedtos.nikki.controllers.*;
import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.external.encoder.JasyptEncoder;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteNoteRepository;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteUserRepository;
import net.yoedtos.nikki.presenters.LoadNotesPresenter;
import net.yoedtos.nikki.presenters.ports.Presenter;
import net.yoedtos.nikki.usecases.createnote.CreateNote;
import net.yoedtos.nikki.usecases.dropnote.DropNote;
import net.yoedtos.nikki.usecases.loadnote.LoadNotes;
import net.yoedtos.nikki.usecases.signin.SignIn;
import net.yoedtos.nikki.usecases.signup.SignUp;
import net.yoedtos.nikki.usecases.updatenote.UpdateNote;

public class Factory {
    private static Factory factory;

    public static Factory getInstance() {
        if ( factory == null) {
            factory = new Factory();
        }
        return factory;
    }

    private NitriteNoteRepository noteRepository;
    private NitriteUserRepository userRepository;
    private JasyptEncoder jasyptEncoder;

    public enum Operation { CREATE, DROP, UPDATE, LOAD, SIGNIN, SIGNUP }

    private Factory() {
        this.jasyptEncoder = new JasyptEncoder();
        this.noteRepository = new NitriteNoteRepository();
        this.userRepository = new NitriteUserRepository();
    }

    public Controller makeController(Operation name) {
        switch (name) {
            case CREATE:
                var createNote = new CreateNote(noteRepository, userRepository);
                return new CreateNoteController(createNote);
            case DROP:
                var dropNote = new DropNote(noteRepository);
                return new DropNoteController(dropNote);
            case UPDATE:
                var updateNote = new UpdateNote(noteRepository, userRepository);
                return new UpdateNoteController(updateNote);
            case SIGNIN:
                var signIn = new SignIn(userRepository, jasyptEncoder);
                return new SignInController(signIn);
            case SIGNUP:
                var signUp = new SignUp(userRepository, jasyptEncoder);
                return new SignUpController(signUp);
            default:
            throw new IllegalStateException("Invalid: " + name);
        }
    }

    public Presenter makePresenter(Operation name) {
        switch (name) {
            case LOAD:
                var loadNotes = new LoadNotes(noteRepository);
                return new LoadNotesPresenter(loadNotes);
            default:
                throw new IllegalStateException("Invalid: " + name);
        }
    }
}
