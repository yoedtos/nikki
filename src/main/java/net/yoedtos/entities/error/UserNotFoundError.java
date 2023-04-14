package net.yoedtos.entities.error;

public class UserNotFoundError extends Error {
    public UserNotFoundError() {
        super("User not found.");
    }
}
