package net.yoedtos.usecases.signup;

import io.vavr.concurrent.Future;
import net.yoedtos.entities.UserData;
import net.yoedtos.usecases.signup.ports.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private List<UserData> usersData;

    public InMemoryUserRepository(ArrayList<UserData> usersArray) {
        this.usersData = usersArray;
    }

    @Override
    public Future<List<UserData>> findAllUsers() {
        return Future.of(() -> usersData);
    }

    @Override
    public Future<UserData> findUserByEmail(String email) {
        Optional<UserData> userData = usersData.stream()
                .filter(u -> u.getEmail().equals(email)).findFirst();
        return Future.of(() -> userData.orElse(null));
    }

    @Override
    public Future<UserData> addUser(UserData userData) {
        usersData.add(userData);
        return Future.of(() -> usersData.get(0));
    }
}
