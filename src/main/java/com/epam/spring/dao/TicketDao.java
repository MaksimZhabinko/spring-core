package com.epam.spring.dao;

import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;

import java.util.List;

public interface TicketDao {
    Ticket createTicket(Ticket ticket);
    List<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNumber);
    List<Ticket> getBookedTicketsByEvent(Event event, int pageSize, int pageNumber);
    boolean doesEventExistByUserIdAndEventId(long userId, long eventId);
    boolean deleteById(long ticketId);
}
