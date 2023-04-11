package net.yoedtos.usecases.ports;

import java.util.List;

public interface UserRepository {
    List<UserData> findAllUsers();
    UserData findUserByEmail(String email);
    UserData addUser(UserData userData);
}
