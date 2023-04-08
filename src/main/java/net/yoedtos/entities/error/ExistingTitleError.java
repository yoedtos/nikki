package net.yoedtos.entities.error;

import lombok.EqualsAndHashCode;
import net.yoedtos.entities.UserData;

@EqualsAndHashCode
public class ExistingTitleError extends Error {
    public ExistingTitleError(String title) {
        super("Title " + title + " already exist.");
    }
}
