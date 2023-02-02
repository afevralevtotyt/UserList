package service;

import com.fevralev.exception.UserNonUniqueException;
import com.fevralev.model.User;
import com.fevralev.repository.UserRepository;
import com.fevralev.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    //Проверяем, что метод getAllLogins создает корректный список пользователей
    @Test
    public void getAllLoginsReturnsCorrectUserList() {
        when(userRepository.getUsers())
                .thenReturn(List.of(new User("User1", "pass1"),
                        new User("user2", "pass2")));
        assertThat(userService.getAllLogins()).isEqualTo(List.of("User1", "user2"));
    }

    //Добавление пользователя с пустым логином бросает исключение и метод addUser() не вызывается
    @Test
    public void addingUserWithEmptyLoginThrowsException() {
        assertThatThrownBy(() -> userService.addUser("", "pass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password and login should not be empty");
        verify(userRepository, never()).addUser(any(User.class));
    }

    //Добавление пользователя с пустым паролем бросает исключение и  метод addUser() не вызывается
    @Test
    public void addingUserWithEmptyPasswordThrowsException() {
        assertThatThrownBy(() -> userService.addUser("g", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password and login should not be empty");
        verify(userRepository, never()).addUser(any(User.class));
    }

    //Добавление не уникального пользователя бросает исключение  метод addUser() не вызывается
    @Test
    public void addingNotUniqueUserThrowsException() {
        when(userRepository.getUsers()).thenReturn(List.of(new User("user", "pas")));
        assertThatThrownBy(() -> userService.addUser("user", "pass"))
                .isInstanceOf(UserNonUniqueException.class).hasMessage("Login is not unique");
        verify(userRepository, never()).addUser(any(User.class));
    }

    //При создании корректного пользователя вызывается метод addUser()
    @Test
    void whenCorrectUserIsAddedThenAddUserIsCalled() {
        when(userRepository.getUsers()).thenReturn(List.of());
        userService.addUser("user", "123");
        verify(userRepository)
                .addUser(any());
    }

    //При поиске пользователя по невалилным логину и паролю метод searchUserByLoginAndPassword() возвращает false и метод вызывается не более 1 раза
    @Test
    void searchingUserByLoginAndPassportReturnFalseWhenUserNotValid() {
        when(userRepository.findUserByLoginAndPass(anyString(), anyString())).thenReturn(Optional.empty());
        assertThat(userService.searchUserByLoginAndPassword(anyString(), anyString())).isFalse();
        verify(userRepository, atMostOnce()).findUserByLoginAndPass(anyString(), anyString());
    }

    //При поиске пользователя по валилным логину и паролю метод searchUserByLoginAndPassword() возвращает true и  метод вызывается не более 1 раза
    @Test
    void searchingUserByLoginAndPassportReturnTrueWhenIsValid() {
        when(userRepository.findUserByLoginAndPass("user", "pass")).thenReturn(Optional.of(new User("user", "pass")));
        assertThat(userService.searchUserByLoginAndPassword("user", "pass")).isTrue();
        verify(userRepository, atMostOnce()).findUserByLoginAndPass(anyString(), anyString());
    }

}
