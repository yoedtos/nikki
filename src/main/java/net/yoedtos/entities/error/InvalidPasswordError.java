package net.yoedtos.entities.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InvalidPasswordError extends Error {
    public InvalidPasswordError() {
        super("Invalid password.");
    }
}
