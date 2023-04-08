package net.yoedtos.usecases.signup;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.entities.User;
import net.yoedtos.entities.UserData;
import net.yoedtos.entities.error.ExistingUserError;
import net.yoedtos.usecases.signup.ports.Encoder;
import net.yoedtos.usecases.signup.ports.UserRepository;

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
        var user = this.userRepository.findUserByEmail(userSignUpRequest.getEmail());
        if (user.get() != null) {
            return Future.of(() -> Either.left(new ExistingUserError(userSignUpRequest)));
        }
        var encodedPassword = this.encoder.encode(userSignUpRequest.getPassword());
        this.userRepository.addUser(new UserData(userSignUpRequest.getEmail(), encodedPassword));
        return Future.of(() -> Either.right(userSignUpRequest));
    }
}
