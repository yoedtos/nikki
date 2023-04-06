package net.yoedtos.entities;

import io.vavr.control.Either;
import net.yoedtos.entities.error.InvalidEmailError;
import net.yoedtos.entities.error.InvalidPasswordError;

public class User {
    public final Email email;
    public final Password password;

    private User(Email email, Password password) {
        this.email = email;
        this.password = password;
    }

    public static Either<Error, User> create(UserData userData) {
        var emailOrError = Email.create(userData.getEmail());
        if(emailOrError.isLeft()) {
            return Either.left(new InvalidEmailError(userData.getEmail()));
        }
        var passwordOrError = Password.create(userData.getPassword());
        if(passwordOrError.isLeft()) {
            return Either.left(new InvalidPasswordError());
        }
        var email = emailOrError.get();
        var password = passwordOrError.get();
        return Either.right(new User(email, password));
    }
}
