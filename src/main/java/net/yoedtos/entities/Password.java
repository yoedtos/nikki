package net.yoedtos.entities;

import static io.vavr.control.Either.left;
import static io.vavr.control.Either.right;

import io.vavr.control.Either;
import lombok.Getter;
import net.yoedtos.entities.error.InvalidPasswordError;

public final class Password {
    @Getter
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Either<InvalidPasswordError, Password> create(String password) {
        if (Password.validate(password)) {
            return right(new Password(password));
        }
        return left(new InvalidPasswordError());
    }

    public static boolean validate(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        if(!password.matches(".*\\d.*")) {
            return false;
        }

        if (password.trim().length() < 6) {
            return false;
        }
        return true;
    }
}
