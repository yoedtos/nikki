package net.yoedtos.nikki.entities;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import io.vavr.control.Either;
import lombok.Getter;
import net.yoedtos.nikki.entities.error.InvalidTitleError;

public final class Title {
    @Getter
    private final String value;

    private Title(String value) {
        this.value = value;
    }

    public static Either<InvalidTitleError, Title> create(String title) {
        if (Title.validate(title)) {
            return right(new Title(title));
        }
        return left(new InvalidTitleError(title));
    }

    public static boolean validate(String title) {
        if (title == null || title.isEmpty()) {
            return false;
        }
        if (title.trim().length() < 3 || title.trim().length() > 256) {
            return false;
        }
        return true;
    }
}
