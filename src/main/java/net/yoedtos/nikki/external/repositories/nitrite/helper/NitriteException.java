package net.yoedtos.nikki.external.repositories.nitrite.helper;

public class NitriteException extends RuntimeException {
    public NitriteException() {
        super();
    }

    public NitriteException(String message) {
        super(message);
    }

    public NitriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
