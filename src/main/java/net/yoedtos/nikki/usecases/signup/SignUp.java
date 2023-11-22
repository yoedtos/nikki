package net.yoedtos.nikki.usecases.signup;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.nikki.entities.User;
import net.yoedtos.nikki.entities.error.ExistingUserError;
import net.yoedtos.nikki.usecases.ports.Encoder;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;

public class SignUp {
    private final UserRepository userRepository;
    private final Encoder encoder;

    public SignUp(UserRepository userRepository, Encoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Future<Either<Error, UserData>> perform(UserData userSignUpRequest) {
        var userOrError = User.create(userSignUpRequest);
        if (userOrError.isLeft()) {
            return Future.of(() -> Either.left(userOrError.getLeft()));
        }
        var user = this.userRepository.findByEmail(userSignUpRequest.getEmail());
        if (user != null) {
            return Future.of(() -> Either.left(new ExistingUserError(userSignUpRequest)));
        }
        var encodedPassword = this.encoder.encode(userSignUpRequest.getPassword());
        var userSignUpResponse = this.userRepository.add(new UserData(null, userSignUpRequest.getEmail(), encodedPassword));
        return Future.of(() -> Either.right(userSignUpResponse));
    }
}
