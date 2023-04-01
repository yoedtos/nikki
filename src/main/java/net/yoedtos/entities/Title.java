package net.yoedtos.entities;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import io.vavr.control.Either;
import net.yoedtos.entities.error.InvalidTitleError;

public class Title {
    public final String value;

    private Title(String value) {
        this.value = value;
    }

    public static Either<InvalidTitleError, Title> create(String title) {
        if (Title.validate(title)) {
            return right(new Title(title));
        }
        return left(new InvalidTitleError());
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
