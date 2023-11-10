package net.yoedtos.nikki.entities.error;

public class UnregisteredOwnerError extends Error {
    public UnregisteredOwnerError() {
        super("Owner unregistered.");
    }
}
