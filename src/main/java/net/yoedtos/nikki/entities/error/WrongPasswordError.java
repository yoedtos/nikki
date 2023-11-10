package net.yoedtos.nikki.entities.error;

public class WrongPasswordError extends Error {
    public WrongPasswordError() {
        super("Wrong password.");
    }
}
