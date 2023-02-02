package com.fevralev.service;

import com.fevralev.exception.UserNonUniqueException;
import com.fevralev.model.User;
import com.fevralev.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAllLogins() {
        return userRepository.getUsers().stream().map(User::getLogin).collect(Collectors.toList());
    }

    public void addUser(String login, String password) {
        if (login.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("Password and login should not be empty");
        }
        if (userRepository.getUsers().stream().anyMatch(u -> u.getLogin().equals(login))) {
            throw new UserNonUniqueException("Login is not unique");
        }
        userRepository.addUser(new User(login, password));
    }

    public boolean searchUserByLoginAndPassword(String login, String password) {
        return !userRepository.findUserByLoginAndPass(login, password).isEmpty();
    }

}
