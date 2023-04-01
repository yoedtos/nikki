package net.yoedtos.entities;

import io.vavr.control.Either;
import lombok.Getter;
import net.yoedtos.entities.error.InvalidTitleError;

public class Note {
    @Getter
    private final Title title;
    @Getter
    private final User owner;

    public Note(Title title, User owner) {
        this.title = title;
        this.owner = owner;
    }

    public static Either<Error, Note> create(String title, User owner) {
        var titleOrError = Title.create(title);
        if (titleOrError.isLeft()) {
            return Either.left(new InvalidTitleError());
        }
        return Either.right(new Note(titleOrError.get(), owner));
    }
}
