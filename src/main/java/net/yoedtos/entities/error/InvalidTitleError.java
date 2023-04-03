package net.yoedtos.entities.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class InvalidTitleError extends Error {
    public InvalidTitleError(String title) {
        super("Invalid title: " + title + ".");
    }
}
