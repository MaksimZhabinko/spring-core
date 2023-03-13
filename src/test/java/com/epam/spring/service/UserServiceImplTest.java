package com.epam.spring.service;

import com.epam.spring.dao.UserDao;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.UserEntity;
import com.epam.spring.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl sut;

    @Test
    void shouldReturnUserWhenExistIdPassed() {
        //given
        Optional<User> expected = Optional.of(new UserEntity("Alex", "Alex322@mail.com"));
        when(userDao.getUserById(1)).thenReturn(expected);

        //when
        User actual = sut.getUserById(1);

        //then
        assertThat(actual, is(expected.get()));
    }

    @Test
    void shouldReturnEmptyWhenNotExistIdPassed() {
        //given
        when(userDao.getUserById(anyLong())).thenReturn(Optional.empty());

        //when
        User actual = sut.getUserById(10);

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldReturnUserWhenExistEmailPassed() {
        //given
        Optional<User> expected = Optional.of(new UserEntity("Alex", "Alex322@mail.com"));
        when(userDao.getUserByEmail("Alex322@mail.com")).thenReturn(expected);

        //when
        User actual = sut.getUserByEmail("Alex322@mail.com");

        //then
        assertThat(actual, is(expected.get()));
    }

    @Test
    void shouldReturnEmptyWhenNotExistEmailPassed() {
        //given
        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.empty());

        //when
        User actual = sut.getUserByEmail("test");

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldDeleteUserWhenExistIdPassed() {
        //given
        when(userDao.deleteUser(1)).thenReturn(true);

        //when
        boolean actual = sut.deleteUser(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteUserWhenNotExistIdPassed() {
        //given
        when(userDao.deleteUser(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.deleteUser(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateUserWhenExistIdPassed() {
        //given
        Optional<User> user = Optional.of(new UserEntity("Alex", "Alex322@mail.com"));
        Optional<User> expected = Optional.of(new UserEntity("AlexUpdated", "Alex322@mail.com"));
        when(userDao.updateUser(user.get())).thenReturn(expected);

        //when
        User actual = sut.updateUser(user.get());

        //then
        assertThat(actual, is(expected.get()));
    }

    @Test
    void shouldReturnEmptyUserWhenNotExistIdPassedWhileUpdating() {
        //given
        Optional<User> user = Optional.of(new UserEntity( "Alex", "Alex322@mail.com"));
        when(userDao.updateUser(any(User.class))).thenReturn(Optional.empty());

        //when
        User actual = sut.updateUser(user.get());

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldCreateUser() {
        //given
        Optional<User> user = Optional.of(new UserEntity( "Alex", "Alex322@mail.com"));
        User expected = new UserEntity( "Alex", "Alex322@mail.com");
        when(userDao.createUser(user.get())).thenReturn(expected);

        //when
        User actual = sut.createUser(user.get());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyUsers() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        String name = "Alex";
        List<User> expected = Arrays.asList(new UserEntity("Alex", "Alex322@mail.com"),
                new UserEntity("Alex21", "Alex@mail.ru"),
                new UserEntity( "Alex34", "Mimir@gmail.eu"));
        when(userDao.getUsersByName(name, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<User> actual = sut.getUsersByName(name, pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }
}
