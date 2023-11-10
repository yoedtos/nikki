package net.yoedtos.nikki.entities.error;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ExistingTitleError extends Error {
    public ExistingTitleError(String title) {
        super("Title " + title + " already exist.");
    }
}
