package repository;

import com.fevralev.model.User;
import com.fevralev.repository.UserRepository;
import net.bytebuddy.build.ToStringPlugin;
import net.bytebuddy.build.ToStringPlugin.Exclude;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;

public class UserRepositoryTest {
    UserRepository userRepository;
    User user1;
    User user2;
    User user3;
    List<User> userList;

    //Конфигурируем тесты, чтобы можно было проигнорировать тест с тегом "skipBeforeEach"
    @BeforeEach
    public void setUp(final TestInfo info) {

        final Set<String> testTags = info.getTags();
        if (testTags.stream()
                .anyMatch(tag -> tag.equals("skipBeforeEach"))) {
            return;
        }
        userRepository = new UserRepository();
        user1 = new User("user1", "pass1");
        user2 = new User("user2", "pass2");
        user3 = new User("user3", "pass3");
        userRepository.addUser(user1);
        userRepository.addUser(user2);
        userRepository.addUser(user3);
        userList = List.of(user1, user2, user3);
    }

    //Проверяем, что метод getUsers() из пустого репозитория возвращает пустой список
    @Test
    @Tag("skipBeforeEach")
    public void whenListOfUsersIsEmptyThenReturnEmptyList() {
        userRepository = new UserRepository();
        Assertions.assertThat(userRepository.getUsers()).isEmpty();
    }

    //Проверяем, что список пользователей соотвествует списку полученному методом getUsers
    @Test
    public void whenListOfUsersIsFullThenReturnFullCorrectList() {
        Assertions.assertThat(this.userList).isEqualTo(userRepository.getUsers());
    }

    //Поиск пользователя с существующим логином
    @Test
    public void searchingValidUserByLogin() {
        Assertions.assertThat(userRepository.findUserByLogin("user1")).get().isEqualTo(user1);
    }

    //Поиск не существуещего пользователя возвращает пустой Optional
    @Test
    public void searchingInvalidUserByLoginReturnsEmptyOptional() {
        Assertions.assertThat(userRepository.findUserByLogin("user4")).isEmpty();
    }

    //Поиск существующего польщователя по логину и паролю возвращает этого пользователя
    @Test
    public void searchingValidUserByLoginAndPass() {
        Assertions.assertThat(userRepository.findUserByLoginAndPass("user3", "pass3")).get().isEqualTo(user3);
    }

    //Поиск пользователя с валидным логином и невалидным паролем возвращает пустой Optional
    @Test
    public void searchingUserWithValidLoginInvalidPassReturnsEmptyOptional() {
        Assertions.assertThat(userRepository.findUserByLoginAndPass("user3", "pass2")).isEmpty();
    }

    //Поиск пользователя с невалидным логином и валидным паролем возвращает пустой Optional
    @Test
    public void searchingUserWithInvalidLoginValidPassReturnsEmptyOptional() {
        Assertions.assertThat(userRepository.findUserByLoginAndPass("user23", "pass2")).isEmpty();
    }
}
