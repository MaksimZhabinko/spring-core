package com.epam.spring.dao.impl;

import com.epam.spring.annotation.BindStaticData;
import com.epam.spring.dao.UserDao;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.UserEntity;
import com.epam.spring.util.IdGenerator;
import com.epam.spring.util.Paginator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Setter
public class UserDaoImpl implements UserDao {
    @BindStaticData(fileLocation = "preparedUsers.json", castTo = UserEntity.class)
    private final Map<Long, User> users = new HashMap<>();
    private Paginator<User> paginator;
    private IdGenerator idGenerator;

    @Override
    public Optional<User> getUserById(long userId) {
        LOG.debug("Retrieving a user by {} id...", userId);
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User createUser(User user) {
        user.setId(idGenerator.generateId(UserEntity.class));
        LOG.info("Adding a new user: name - {}, email - {}...", user.getName(), user.getEmail());
        users.put(user.getId(), user);
        LOG.info("The user was added successfully");
        return users.get(user.getId());
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            LOG.info("Updating a user by {} id with the following data: name - {}, email - {}...",
                    user.getId(), user.getName(), user.getEmail());
            users.put(user.getId(), user);
            LOG.info("The user was updated successfully");
            return Optional.of(users.get(user.getId()));
        }
        LOG.warn("Such user was not found while updating");
        return Optional.empty();
    }

    @Override
    public boolean deleteUser(long userId) {
        LOG.info("Deleting a user by {} id...", userId);
        if (!users.containsKey(userId)) {
            LOG.warn("No user was found with such id");
            return false;
        }
        users.remove(userId);
        LOG.info("The user was deleted successfully");
        return true;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        LOG.info("Retrieving a user by {}...", email);
        for (Map.Entry<Long, User> user : users.entrySet()) {
            if (user.getValue().getEmail().equals(email)) {
                return Optional.of(user.getValue());
            }
        }
        LOG.warn("No user was found with such email {}", email);
        return Optional.empty();
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNumber) {
        return paginator.paginate(this.users
                .values()
                .stream()
                .filter(userEntry -> userEntry.getName().contains(name))
                .toList(), pageSize, pageNumber);
    }
}
