package net.yoedtos.nikki.entities;

import io.vavr.control.Either;
import lombok.Getter;
import net.yoedtos.nikki.entities.error.InvalidEmailError;

import java.util.regex.Pattern;

public final class Email {
    @Getter
    private final String value;
    private static final String regex = "^[-!#$%&'*+/0-9=?A-Z^_a-z`{|}~]" +
            "(\\.?[-!#$%&'*+/0-9=?A-Z^_a-z`{|}~])*@[a-zA-Z0-9](-*\\.?[a-zA-Z0-9])*\\.[a-zA-Z](-?[a-zA-Z0-9])+$";

    private Email(String email) {
        this.value = email;
    }

    public static Either<InvalidEmailError, Email> create(String email) {
        if(Email.validate(email)) {
            return Either.right(new Email(email));
        }
        return Either.left(new InvalidEmailError(email));
    }

    public static boolean validate(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        if (Email.isNotConformant(email)) {
            return false;
        }

        var emailArray = email.split("@");

        if(emailArray[0].isEmpty() || emailArray[0].length() > 64) {
            return false;
        }

        if(emailArray.length < 2 || emailArray[1].length() > 254) {
            return false;
        }

        if(emailArray[1].split(Pattern.quote("."))[0].length() > 63) {
            return false;
        }
        return true;
    }

    private static boolean isNotConformant(String email) {
        return !Pattern.compile(regex).matcher(email).matches();
    }
}
