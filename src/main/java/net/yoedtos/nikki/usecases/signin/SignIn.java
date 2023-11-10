package net.yoedtos.nikki.usecases.signin;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.entities.error.UserNotFoundError;
import net.yoedtos.nikki.entities.error.WrongPasswordError;
import net.yoedtos.nikki.usecases.ports.Encoder;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;

public class SignIn {
    private UserRepository userRepository;
    private Encoder encoder;

    public SignIn(UserRepository userRepository, Encoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Future<Either<Error,UserData>> perform(UserData validUser) {
        var user = this.userRepository.findByEmail(validUser.getEmail());
        if (user == null) {
            return Future.of(() -> Either.left(new UserNotFoundError()));
        }
        var valid = encoder.compare(validUser.getPassword(), user.getPassword());
        if (!valid) {
            return Future.of(() -> Either.left(new WrongPasswordError()));
        }
        return Future.of(() -> Either.right(validUser));
    }
}
