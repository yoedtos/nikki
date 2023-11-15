package net.yoedtos.nikki.external.repositories.nitrite;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteException;
import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteHelper;
import net.yoedtos.nikki.usecases.ports.UserData;
import net.yoedtos.nikki.usecases.ports.UserRepository;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.util.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

public class NitriteUserRepository implements UserRepository, Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NitriteUserRepository.class);

    private NitriteHelper helper;
    private ObjectRepository<User> userRepo;

    public NitriteUserRepository() {
        this.helper = new NitriteHelper();
        userRepo = helper.getRepository(User.class);
    }

    @Override
    public UserData add(UserData userData) {
        try {
            var note = toUser(userData);
            var result = userRepo.insert(note);
            var id = Iterables.firstOrDefault(result).getIdValue();
            return new UserData(id,
                    userData.getEmail(),
                    userData.getPassword());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public List<UserData> findAll() {
        var notes = new ArrayList<UserData>();
        try {
            var cursor = userRepo.find();
            cursor.forEach(user -> {
                notes.add(new UserData(user.getId().getIdValue(),
                        user.getEmail(),
                        user.getPassword()));
            });
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
        return notes;
    }

    @Override
    public UserData findByEmail(String email) {
        try {
            var cursor = userRepo.find(eq("email", email));
            var user = cursor.firstOrDefault();
            return toUserData(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new NitriteException(e.getMessage());
        }
    }

    @Override
    public void close() {
        helper.close();
    }

    private User toUser(UserData userData) {
        return new User(userData.getEmail(),
                        userData.getPassword());
    }

    private UserData toUserData(User user) {
        return new UserData(user.getId().getIdValue(),
                            user.getEmail(),
                            user.getPassword());
    }
}

class User {
    @Id
    private NitriteId id;
    private String email;
    private String password;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public NitriteId getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
