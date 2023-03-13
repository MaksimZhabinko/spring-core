package com.epam.spring.dao;

import com.epam.spring.dao.impl.EventDaoImpl;
import com.epam.spring.model.Event;
import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.util.IdGenerator;
import com.epam.spring.util.Paginator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventDaoImplTest {
    @Mock
    private Paginator paginator;
    @Mock
    private IdGenerator idGenerator;
    @InjectMocks
    private EventDaoImpl sut;

    @Test
    void shouldReturnEventWhenExistIdPassed() {
        //given
        Event expected = createEvent();

        //when
        Optional<Event> actual = sut.getEventById(1);

        //then
        assertThat(actual.get(), is(expected));
    }

    @Test
    void shouldReturnEmptyWhenNotExistIdPassed() {
        //given
        createEvent();

        //when
        Optional<Event> actual = sut.getEventById(10);

        //then
        assertFalse(actual.isPresent());
    }

    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        createEvent();

        //when
        boolean actual = sut.deleteEvent(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        createEvent();

        //when
        boolean actual = sut.deleteEvent(10);

        //then
        assertFalse(actual);
    }

    @Test
    void shouldUpdateEventWhenExistIdPassed() {
        //given
        createEvent();
        Event expected = new EventEntity("New Year Updated", new Date());
        expected.setId(1L);

        //when
        EventEntity newEvent = new EventEntity("New Year Updated", new Date());
        newEvent.setId(1L);
        Optional<Event> actual = sut.updateEvent(newEvent);

        //then
        assertThat(actual.get(), is(expected));
    }

    @Test
    void shouldReturnEmptyEventWhenNotExistIdPassedWhileUpdating() {
        //given
        createEvent();
        Optional<Event> expected = Optional.empty();

        //when
        EventEntity newEvent = new EventEntity("New Year Updated", new Date());
        newEvent.setId(10L);
        Optional<Event> actual = sut.updateEvent(newEvent);

        //then
        assertThat(actual, is(expected));
    }

    public Event createEvent() {
        when(idGenerator.generateId(EventEntity.class)).thenReturn(1L);
        Event expected = new EventEntity("New Year", new Date());
        expected.setId(1L);
        sut.createEvent(new EventEntity("New Year", new Date()));
        return expected;
    }
}
