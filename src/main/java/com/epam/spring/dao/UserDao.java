package com.epam.spring.dao;

import com.epam.spring.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long userId);
    User createUser(User user);
    Optional<User> updateUser(User user);
    boolean deleteUser(long userId);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByName(String name, int pageSize, int pageNumber);
}
