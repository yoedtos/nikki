package net.yoedtos.nikki.usecases.ports;

import java.util.List;

public interface UserRepository {
    List<UserData> findAll();
    UserData findByEmail(String email);
    UserData add(UserData userData);
}
