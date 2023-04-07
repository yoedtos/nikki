package net.yoedtos.usecases.signup.ports;

import io.vavr.concurrent.Future;
import net.yoedtos.entities.UserData;

import java.util.List;

public interface UserRepository {
    Future<List<UserData>> findAllUsers();
    Future<UserData> findUserByEmail(String email);
    Future<UserData> addUser(UserData userData);
}
