package net.yoedtos.nikki.entities.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InvalidEmailError extends Error {
    public InvalidEmailError(String email) {
        super("Invalid email: " + email + ".");
    }
}
