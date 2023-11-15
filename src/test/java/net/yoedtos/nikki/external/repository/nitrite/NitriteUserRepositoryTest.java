package net.yoedtos.nikki.external.repository.nitrite;

import static net.yoedtos.nikki.usecases.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.yoedtos.nikki.external.repositories.nitrite.helper.NitriteException;
import net.yoedtos.nikki.external.repositories.nitrite.NitriteUserRepository;
import net.yoedtos.nikki.usecases.ports.UserData;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.OrderWith;
import org.junit.runner.manipulation.Alphanumeric;

@OrderWith(Alphanumeric.class)
public class NitriteUserRepositoryTest {

    private static NitriteUserRepository userRepository;

    @BeforeClass
    public static void begin() {
        userRepository = new NitriteUserRepository();
    }

    @Test
    public void A_shouldAddValidUser() {
        var addedUser = userRepository.add(DB_USER);
        assertThat(addedUser.getId()).isNotNull();
        var userData = userRepository.findByEmail(addedUser.getEmail());
        assertThat(userData).isNotNull();
    }

    @Test
    public void B_shouldFindAllUsers() {
        var userTwo = new UserData(1L, "usertwo@email.com", "secret2");
        userRepository.add(userTwo);
        var notes = userRepository.findAll();
        assertThat(notes).hasSize(2);
    }

    @Test
    public void C_shouldNotFindInvalidUser() {
        assertThatThrownBy(() -> {
            userRepository.findByEmail("unreal@email.com");
        }).isInstanceOf(NitriteException.class);
    }
}
