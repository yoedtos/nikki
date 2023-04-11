package net.yoedtos.usecases.doubles.repositories;

import net.yoedtos.usecases.ports.UserData;
import net.yoedtos.usecases.ports.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private List<UserData> usersData;

    public InMemoryUserRepository(ArrayList<UserData> usersArray) {
        this.usersData = usersArray;
    }

    @Override
    public List<UserData> findAllUsers() {
        return usersData;
    }

    @Override
    public UserData findUserByEmail(String email) {
        Optional<UserData> userData = usersData.stream()
                .filter(u -> u.getEmail().equals(email)).findFirst();
        return userData.orElse(null);
    }

    @Override
    public UserData addUser(UserData userData) {
        var size = usersData.size();
        usersData.add(new UserData(Long.valueOf(size), userData.getEmail(), userData.getPassword()));
        return usersData.get(0);
    }
}
