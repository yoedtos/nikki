package net.yoedtos.entities.error;

public class UnregisteredOwnerError extends Error {
    public UnregisteredOwnerError() {
        super("Owner unregistered.");
    }
}
