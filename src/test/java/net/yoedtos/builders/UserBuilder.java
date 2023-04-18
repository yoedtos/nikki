package net.yoedtos.builders;

import static net.yoedtos.usecases.TestConstant.*;

import net.yoedtos.usecases.ports.UserData;

public final class UserBuilder {

    private UserData userData = new UserData(VALID_USER_ID, VALID_EMAIL, VALID_PASSWORD);

    private UserBuilder() {}

    public static UserBuilder create() {
        return new UserBuilder();
    }

    public UserBuilder withUnregisteredEmail() {
        this.userData = new UserData(null, "unknow@mail.com", "passworD456");
        return this;
    }

    public UserBuilder withInvalidEmail() {
        this.userData = new UserData(null, INVALID_EMAIL, VALID_PASSWORD);
        return this;
    }

    public UserBuilder withInvalidPassword() {
        this.userData = new UserData(null, VALID_EMAIL, INVALID_PASSWORD);
        return this;
    }

    public UserBuilder withWrongPassword() {
        this.userData = new UserData(null, VALID_EMAIL, "drowssaP321");
        return this;
    }

    public final UserData build() {
        return userData;
    }
}
