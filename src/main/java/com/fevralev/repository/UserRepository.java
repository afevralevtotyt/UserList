package com.fevralev.repository;

import com.fevralev.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public Optional<User> findUserByLogin(String login) {
        return users.stream()
                .filter(u -> u.getLogin().equals(login))
                .findAny();
    }

    public Optional<User> findUserByLoginAndPass(String login, String password) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login) && user.getPassword().equals(password))
                .findAny();
    }

    public void addUser(User user) {
        users.add(user);
    }
}
