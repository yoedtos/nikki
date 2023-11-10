package net.yoedtos.nikki.usecases.doubles.repositories;

import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private List<UserData> usersData;

    public InMemoryUserRepository(ArrayList<UserData> usersArray) {
        this.usersData = usersArray;
    }

    @Override
    public List<UserData> findAll() {
        return usersData;
    }

    @Override
    public UserData findByEmail(String email) {
        Optional<UserData> userData = usersData.stream()
                .filter(u -> u.getEmail().equals(email)).findFirst();
        return userData.orElse(null);
    }

    @Override
    public UserData add(UserData userData) {
        var size = usersData.size();
        usersData.add(new UserData(Long.valueOf(size), userData.getEmail(), userData.getPassword()));
        return usersData.get(0);
    }
}
