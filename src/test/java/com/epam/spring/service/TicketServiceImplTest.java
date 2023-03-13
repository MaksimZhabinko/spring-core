package com.epam.spring.service;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.epam.spring.dao.EventDao;
import com.epam.spring.dao.TicketDao;
import com.epam.spring.dao.UserDao;
import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.model.entity.TicketEntity;
import com.epam.spring.model.entity.UserEntity;
import com.epam.spring.service.impl.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {
    @Mock
    private EventDao eventDao;
    @Mock
    private UserDao userDao;
    @Mock
    private TicketDao ticketDao;
    @InjectMocks
    private TicketServiceImpl sut;

    @Test
    void shouldBookWhenExistIdsPassedAndBookingNotAlreadyDone() {
        //given
        Optional<Event> event = createEvent(1, "New Year", new Date());
        Optional<User> user = createUser(1, "Alex", "Alex322@mail.com");
        Ticket expected = createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2);
        when(userDao.getUserById(1)).thenReturn(user);
        when(eventDao.getEventById(1)).thenReturn(event);
        when(ticketDao.doesEventExistByUserIdAndEventId(1, 1)).thenReturn(false);
        when(ticketDao.createTicket(any(TicketEntity.class))).thenReturn(expected);

        //when
        Ticket actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldNotBookWhenNotExistUserIdPassed() {
        //given
        when(userDao.getUserById(1)).thenReturn(Optional.empty());

        //when
        Ticket actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldNotBookWhenNotExistEventIdPassed() {
        //given
        when(userDao.getUserById(1)).thenReturn(createUser(1, "Alex", "Alex322@mail.com"));
        when(eventDao.getEventById(1)).thenReturn(Optional.empty());

        //when
        Ticket actual = sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM);

        //then
        assertThat(actual, nullValue());
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenBookingAlreadyDone() {
        //when
        Optional<Event> event = createEvent(1, "New Year", new Date());
        Optional<User> user = createUser(1, "Alex", "Alex322@mail.com");
        when(userDao.getUserById(1)).thenReturn(user);
        when(eventDao.getEventById(1)).thenReturn(event);
        when(ticketDao.doesEventExistByUserIdAndEventId(1, 1)).thenReturn(true);

        //then
        assertThrows(IllegalStateException.class, () -> sut.bookTicket(1, 1, 2, Ticket.Category.PREMIUM));
    }

    @Test
    void shouldGetBookingsByUser() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        Optional<User> user = createUser(1, "Alex", "Alex322@mail.com");

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5),
                createTicket(10, 1, 3, Ticket.Category.STANDARD, 10));

        List<Event> events = Arrays.asList(createEvent(1, "New Year", new Date(123)).get(),
                createEvent(2, "New Year After-party", new Date(233)).get(),
                createEvent(3, "New Year Pre_party", new Date(344)).get());

        List<Ticket> expected = Arrays.asList(createTicket(10, 1, 3, Ticket.Category.STANDARD, 10),
                createTicket(1, 1, 2, Ticket.Category.PREMIUM, 2),
                createTicket(3, 1, 1, Ticket.Category.BAR, 5));

        when(ticketDao.getBookedTicketsByUser(user.get(), pageSize, pageNumber)).thenReturn(tickets);
        when(eventDao.getEventById(1)).thenReturn(Optional.of(events.get(0)));
        when(eventDao.getEventById(2)).thenReturn(Optional.of(events.get(1)));
        when(eventDao.getEventById(3)).thenReturn(Optional.of(events.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByUser(user.get(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));
    }

    @Test
    void shouldGetBookingsByEvent() {
        //given
        int pageSize = 5;
        int pageNumber = 2;

        Optional<Event> event = createEvent(1, "New Year", new Date(123));

        List<Ticket> tickets = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10));

        List<User> users = Arrays.asList(createUser(1, "Alex", "Alex322@mail.com").get(),
                createUser(2, "Alex21", "Mimir@gmail.eu").get(),
                createUser(3, "Alex34", "BAlex322@mail.ru").get());

        List<Ticket> expected = Arrays.asList(createTicket(1, 1, 1, Ticket.Category.PREMIUM, 2),
                createTicket(10, 3, 1, Ticket.Category.STANDARD, 10),
                createTicket(3, 2, 1, Ticket.Category.BAR, 5));

        when(ticketDao.getBookedTicketsByEvent(event.get(), pageSize, pageNumber)).thenReturn(tickets);
        when(userDao.getUserById(1)).thenReturn(Optional.of(users.get(0)));
        when(userDao.getUserById(2)).thenReturn(Optional.of(users.get(1)));
        when(userDao.getUserById(3)).thenReturn(Optional.of(users.get(2)));

        //when
        List<Ticket> actual = sut.getBookedTicketsByEvent(event.get(), pageSize, pageNumber);

        //then
        assertThat(actual, is(expected));

    }


    @Test
    void shouldDeleteEventWhenExistIdPassed() {
        //given
        when(ticketDao.deleteById(1)).thenReturn(true);

        //when
        boolean actual = sut.cancelTicket(1);

        //then
        assertTrue(actual);
    }

    @Test
    void shouldNotDeleteEventWhenNotExistIdPassed() {
        //given
        when(ticketDao.deleteById(anyLong())).thenReturn(false);

        //when
        boolean actual = sut.cancelTicket(10);

        //then
        assertFalse(actual);
    }

    private Optional<Event> createEvent(long id, String title, Date date) {
        EventEntity event = new EventEntity(title, date);
        event.setId(id);
        return Optional.of(event);
    }

    private Optional<User> createUser(long id, String name, String email) {
        UserEntity user = new UserEntity(name, email);
        user.setId(id);
        return Optional.of(user);
    }

    private Ticket createTicket(long id, long userId, long eventId, Ticket.Category category, int place) {
        Ticket ticket = new TicketEntity(userId, eventId, category, place);
        ticket.setId(id);
        return ticket;
    }

}
