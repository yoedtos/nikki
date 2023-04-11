package net.yoedtos.usecases.signup;

import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import net.yoedtos.entities.User;
import net.yoedtos.usecases.ports.UserData;
import net.yoedtos.entities.error.ExistingUserError;
import net.yoedtos.usecases.ports.Encoder;
import net.yoedtos.usecases.ports.UserRepository;

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
        var userSignUpResponse = this.userRepository.addUser(new UserData(null, userSignUpRequest.getEmail(), encodedPassword)).get();
        return Future.of(() -> Either.right(userSignUpResponse));
    }
}
