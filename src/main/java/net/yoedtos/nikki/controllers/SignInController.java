package net.yoedtos.nikki.controllers;

import net.yoedtos.nikki.controllers.ports.Controller;
import net.yoedtos.nikki.controllers.ports.EnterDTO;
import net.yoedtos.nikki.main.MainApp;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.signin.SignIn;

public class SignInController implements Controller<EnterDTO> {
    private final SignIn signInUseCase;

    public SignInController(SignIn useCase) {
        this.signInUseCase = useCase;
    }

    @Override
    public void handle(EnterDTO enter) {
        var userData = new UserData(null, enter.getEmail(), enter.getPassword());
        var result = signInUseCase.perform(userData).get();
        if (result.isRight()) {
            MainApp.setUserId(result.get().getId());
            MainApp.setUserEmail(result.get().getEmail());
        }
        if (result.isLeft()) {
            throw result.getLeft();
        }
    }
}
