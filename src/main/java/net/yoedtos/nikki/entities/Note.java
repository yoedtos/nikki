package net.yoedtos.nikki.entities;

import io.vavr.control.Either;
import lombok.Getter;
import net.yoedtos.nikki.entities.error.InvalidTitleError;

public final class Note {
    @Getter
    private final Title title;
    @Getter
    private final String content;
    @Getter
    private final User owner;

    public Note(Title title, String content, User owner) {
        this.title = title;
        this.content = content;
        this.owner = owner;
    }

    public static Either<Error, Note> create(String title, String content, User owner) {
        var titleOrError = Title.create(title);
        if (titleOrError.isLeft()) {
            return Either.left(new InvalidTitleError(title));
        }
        return Either.right(new Note(titleOrError.get(), content, owner));
    }
}
