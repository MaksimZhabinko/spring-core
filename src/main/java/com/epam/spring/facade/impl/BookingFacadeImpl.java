package com.epam.spring.facade.impl;

import com.epam.spring.facade.BookingFacade;
import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;
import com.epam.spring.service.impl.EventServiceImpl;
import com.epam.spring.service.impl.TicketServiceImpl;
import com.epam.spring.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class BookingFacadeImpl implements BookingFacade {
    private final EventServiceImpl eventServiceImpl;
    private final TicketServiceImpl ticketServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @Override
    public Event getEventById(long eventId) {
        return eventServiceImpl.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNumber) {
        return eventServiceImpl.getEventsByTitle(title, pageSize, pageNumber);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNumber) {
        return eventServiceImpl.getEventsForDay(day, pageSize, pageNumber);
    }

    @Override
    public Event createEvent(Event event) {
        return eventServiceImpl.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventServiceImpl.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventServiceImpl.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userServiceImpl.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userServiceImpl.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNumber) {
        return userServiceImpl.getUsersByName(name, pageSize, pageNumber);
    }

    @Override
    public User createUser(User user) {
        return userServiceImpl.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userServiceImpl.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userServiceImpl.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return ticketServiceImpl.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNumber) {
        return ticketServiceImpl.getBookedTicketsByUser(user, pageSize, pageNumber);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNumber) {
        return ticketServiceImpl.getBookedTicketsByEvent(event, pageSize, pageNumber);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketServiceImpl.cancelTicket(ticketId);
    }
}
