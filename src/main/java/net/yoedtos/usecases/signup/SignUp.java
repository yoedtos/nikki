package net.yoedtos.usecases.signup;

import io.vavr.concurrent.Future;
import net.yoedtos.entities.UserData;
import net.yoedtos.usecases.signup.ports.Encoder;
import net.yoedtos.usecases.signup.ports.UserRepository;

public class SignUp {
    private final UserRepository userRepository;
    private final Encoder encoder;

    public SignUp(UserRepository userRepository, Encoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public Future<UserData> perform(UserData userSignUpRequest) {
        var encodedPassword = this.encoder.encode(userSignUpRequest.getPassword());
        this.userRepository.addUser(new UserData(userSignUpRequest.getEmail(), encodedPassword));
        return Future.of(() -> userSignUpRequest);
    }
}
