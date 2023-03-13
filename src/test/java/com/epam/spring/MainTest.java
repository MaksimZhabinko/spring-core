package com.epam.spring;

import com.epam.spring.facade.impl.BookingFacadeImpl;
import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    @Test
    void realLifeTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        BookingFacadeImpl bookingFacadeImpl = context.getBean("bookingFacade", BookingFacadeImpl.class);
        User user = new UserEntity("NewUser", "NewUser@mail.com");
        User createdUser = bookingFacadeImpl.createUser(user);

        assertThat(createdUser.getId(), is(5L));

        Event event = new EventEntity("NewEvent", new Date(333));

        Event createdEvent = bookingFacadeImpl.createEvent(event);

        assertThat(createdEvent.getId(), is(5L));

        Ticket ticket = bookingFacadeImpl.bookTicket(createdUser.getId(), createdEvent.getId(), 2, Ticket.Category.PREMIUM);

        assertThat(ticket.getId(), is(6L));
        assertThat(ticket.getUserId(), is(5L));
        assertThat(ticket.getEventId(), is(5L));
        assertThat(ticket.getCategory(), is(Ticket.Category.PREMIUM));
        assertThat(ticket.getPlace(), is(2));

        List<Ticket> bookedTickets = bookingFacadeImpl.getBookedTickets(createdUser, 10, 1);

        assertThat(bookedTickets, is(Collections.singletonList(ticket)));

        boolean isCanceled = bookingFacadeImpl.cancelTicket(ticket.getId());
        List<Ticket> bookedTicketsAfterCancellation = bookingFacadeImpl.getBookedTickets(createdUser, 10, 1);

        assertTrue(isCanceled);
        assertThat(bookedTicketsAfterCancellation, is(Collections.emptyList()));
    }
}
