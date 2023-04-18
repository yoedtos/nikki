package net.yoedtos.builders;

import static net.yoedtos.usecases.TestConstant.*;

import net.yoedtos.usecases.ports.NoteData;

public final class NoteBuilder {

    private NoteData noteData = new NoteData(NOTE_ID, VALID_USER_ID, VALID_EMAIL, VALID_TITLE, VALID_CONTENT);

    private NoteBuilder() {}

    public static NoteBuilder create() {
        return new NoteBuilder();
    }

    public NoteBuilder withInvalidTitle() {
        this.noteData = new NoteData(NOTE_ID, VALID_USER_ID, VALID_EMAIL, INVALID_TITLE,  VALID_CONTENT);
        return this;
    }

    public NoteBuilder withUnregisteredOwner() {
        this.noteData = new NoteData(null, 10L, "unknow@mail.com", VALID_TITLE,  VALID_CONTENT);
        return this;
    }

    public final NoteData build() {
        return this.noteData;
    }
}
