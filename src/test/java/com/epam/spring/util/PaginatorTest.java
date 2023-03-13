package com.epam.spring.util;

import com.epam.spring.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PaginatorTest {
    @InjectMocks
    private Paginator<UserEntity> sut;

    @Test
    void shouldPaginateEntities() {
        //given
        List<UserEntity> users = Arrays.asList(new UserEntity("Alex1", "Alex322@mail.ru"),
                new UserEntity("Alex2", "Alex322@mail.ru"),
                new UserEntity("Alex3", "Alex322@mail.ru"),
                new UserEntity("Alex4", "Alex322@mail.ru"),
                new UserEntity("Alex5", "Alex322@mail.ru"),
                new UserEntity("Alex6", "Alex322@mail.ru"),
                new UserEntity("Alex7", "Alex322@mail.ru"),
                new UserEntity("Alex8", "Alex322@mail.ru"),
                new UserEntity("Alex9", "Alex322@mail.ru"),
                new UserEntity("Alex10", "Alex322@mail.ru"));

        List<UserEntity> expected1 = Arrays.asList(new UserEntity("Alex4", "Alex322@mail.ru"),
                new UserEntity("Alex5", "Alex322@mail.ru"),
                new UserEntity("Alex6", "Alex322@mail.ru"));

        List<UserEntity> expected2 = Arrays.asList(new UserEntity("Alex10", "Alex322@mail.ru"));

        List<UserEntity> expected3 = Arrays.asList(new UserEntity("Alex7", "Alex322@mail.ru"),
                new UserEntity("Alex8", "Alex322@mail.ru"),
                new UserEntity("Alex9", "Alex322@mail.ru"),
                new UserEntity("Alex10", "Alex322@mail.ru"));


        //when
        List<UserEntity> actual1 = sut.paginate(users, 3, 2);
        List<UserEntity> actual2 = sut.paginate(users, 3, 4);
        List<UserEntity> actual3 = sut.paginate(users, 6, 2);
        List<UserEntity> actual4 = sut.paginate(users, 6, 3);

        //then
        assertThat(actual1, is(expected1));
        assertThat(actual2, is(expected2));
        assertThat(actual3, is(expected3));
        assertThat(actual4, is(Collections.emptyList()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageSizePassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 0, 10));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenInvalidPageNumberPassed() {
        assertThrows(IllegalArgumentException.class, () -> sut.paginate(Collections.emptyList(), 2, -2));
    }
}
