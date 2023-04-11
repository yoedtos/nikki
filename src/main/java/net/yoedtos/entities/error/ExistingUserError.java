package net.yoedtos.entities.error;

import lombok.EqualsAndHashCode;
import net.yoedtos.usecases.ports.UserData;

@EqualsAndHashCode
public class ExistingUserError extends Error {
    public ExistingUserError(UserData userData) {
        super("User " + userData.getEmail() + " already registered.");
    }
}
