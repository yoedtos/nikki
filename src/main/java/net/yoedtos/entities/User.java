package net.yoedtos.entities;

import io.vavr.control.Either;
import net.yoedtos.entities.error.InvalidEmailError;

public class User {
    public final Email email;

    private User(Email email) {
        this.email = email;
    }

    public static Either<InvalidEmailError, User> create(UserData userData) {
        var emailOrError = Email.create(userData.getEmail());
        if(emailOrError.isLeft()) {
            return Either.left(new InvalidEmailError(userData.getEmail()));
        }
        var email = emailOrError.get();
        return Either.right(new User(email));
    }
}
