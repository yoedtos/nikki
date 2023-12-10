package net.yoedtos.nikki.external.repositories.nitrite;

import static net.yoedtos.nikki.usecases.TestConstant.DB_USER;
import static net.yoedtos.nikki.usecases.TestConstant.VALID_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;

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
        var userData = userRepository.findByEmail("unreal@email.com");
        assertThat(userData).isNull();
    }

    @Test
    public void D_shouldFindUserByEmail() {
        var userData = userRepository.findByEmail(VALID_EMAIL);
        assertThat(userData.getEmail()).isEqualTo(VALID_EMAIL);
    }
}
