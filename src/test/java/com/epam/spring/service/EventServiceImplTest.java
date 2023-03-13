package com.epam.spring.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.epam.spring.dao.EventDao;
import com.epam.spring.model.Event;
import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventDao dao;
    @InjectMocks
    private EventServiceImpl sut;

    @Test
    void shouldReturnEventWhenExistIdPassed() {
        //given
        Optional<Event> expected = Optional.of(new EventEntity("New Year", new Date()));
        when(dao.getEventById(1)).thenReturn(expected);

        //when
        Event actual = sut.getEventById(1);

        //then
        assertThat(actual, is(expected.get()));
    }

    @Test
    void shouldReturnEmptyWhenNotExistIdPassed() {
        //given
        when(dao.getEventById(anyLong())).thenReturn(Optional.empty());

        //when
        Event actual = sut.getEventById(10);

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        when(dao.deleteEvent(1)).thenReturn(true);

        //when
        boolean actual = sut.deleteEvent(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        when(dao.deleteEvent(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.deleteEvent(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateEventWhenExistIdPassed() {
        //given
        Optional<Event> user = Optional.of(new EventEntity("New Year", new Date()));
        Optional<Event> expected = Optional.of(new EventEntity("New Year Updated", new Date()));
        when(dao.updateEvent(user.get())).thenReturn(expected);

        //when
        Event actual = sut.updateEvent(user.get());

        //then
        assertThat(actual, is(expected.get()));
    }

    @Test
    void shouldReturnEmptyEventWhenNotExistIdPassedWhileUpdating() {
        //given
        Optional<Event> user = Optional.of(new EventEntity("New Year", new Date()));
        when(dao.updateEvent(any(Event.class))).thenReturn(Optional.empty());

        //when
        Event actual = sut.updateEvent(user.get());

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldCreateEvent() {
        //given
        Optional<Event> event = Optional.of(new EventEntity("New Year", new Date()));
        Event expected = new EventEntity("New Year", new Date());
        when(dao.createEvent(event.get())).thenReturn(expected);

        //when
        Event actual = sut.createEvent(event.get());

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyEventsBtTitle() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        String title = "New Year";
        List<Event> expected = Arrays.asList(new EventEntity("New Year", new Date()),
                new EventEntity("New Year After-party", new Date()),
                new EventEntity("New Year Pre_party", new Date()));
        when(dao.getEventsByTitle(title, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<Event> actual = sut.getEventsByTitle(title, pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldReturnNonEmptyEventsByDay() {
        //given
        int pageSize = 5;
        int pageNumber = 2;
        Date date = new Date(123L);
        List<Event> expected = Arrays.asList(new EventEntity("New Year", new Date(123L)),
                new EventEntity("New Year After-party", new Date(123L)),
                new EventEntity("New Year Pre_party", new Date(123L)));
        when(dao.getEventsForDay(date, pageSize, pageNumber)).thenReturn(expected);

        //when
        List<Event> actual = sut.getEventsForDay(new Date(123L), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }
}
