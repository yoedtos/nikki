package net.yoedtos.nikki.entities.error;

import lombok.EqualsAndHashCode;
import net.yoedtos.nikki.usecases.ports.UserData;

@EqualsAndHashCode
public class ExistingUserError extends Error {
    public ExistingUserError(UserData userData) {
        super("User " + userData.getEmail() + " already registered.");
    }
}
