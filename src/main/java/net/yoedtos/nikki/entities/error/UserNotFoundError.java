package net.yoedtos.nikki.entities.error;

public class UserNotFoundError extends Error {
    public UserNotFoundError() {
        super("User not found.");
    }
}
