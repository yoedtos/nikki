package net.yoedtos.nikki.entities.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InvalidPasswordError extends Error {
    public InvalidPasswordError() {
        super("Invalid password.");
    }
}
