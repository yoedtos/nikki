package net.yoedtos.entities.error;

public class WrongPasswordError extends Error {
    public WrongPasswordError() {
        super("Wrong password.");
    }
}
