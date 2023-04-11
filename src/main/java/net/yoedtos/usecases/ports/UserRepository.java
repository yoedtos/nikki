package net.yoedtos.usecases.ports;

import io.vavr.concurrent.Future;

import java.util.List;

public interface UserRepository {
    Future<List<UserData>> findAllUsers();
    Future<UserData> findUserByEmail(String email);
    Future<UserData> addUser(UserData userData);
}
