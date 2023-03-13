package com.epam.spring.dao.impl;

import com.epam.spring.annotation.BindStaticData;
import com.epam.spring.dao.TicketDao;
import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.TicketEntity;
import com.epam.spring.util.IdGenerator;
import com.epam.spring.util.Paginator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@Setter
public class TicketDaoImpl implements TicketDao {
    @BindStaticData(fileLocation = "preparedTickets.json", castTo = TicketEntity.class)
    private final Map<Long, Ticket> tickets = new HashMap<>();
    private Paginator<Ticket> paginator;
    private IdGenerator idGenerator;

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setId(idGenerator.generateId(TicketEntity.class));
        LOG.info("Adding a new ticket: userId - {}, eventId - {}, place - {}, category - {}...",
                ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        tickets.put(ticket.getId(), ticket);
        LOG.debug("The event was added successfully");
        return tickets.get(ticket.getId());
    }

    @Override
    public List<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNumber) {
        LOG.info("Retrieving tickets by user id {}... Passed page size - {}, page number - {}", user.getId(), pageSize, pageNumber);
        return filter(ticket -> ticket.getUserId() == user.getId(), pageSize, pageNumber);
    }

    @Override
    public List<Ticket> getBookedTicketsByEvent(Event event, int pageSize, int pageNumber) {
        LOG.info("Retrieving tickets by event id {}... Passed page size - {}, page number - {}", event.getId(), pageSize, pageNumber);
        return filter(ticket -> ticket.getEventId() == event.getId(), pageSize, pageNumber);
    }

    @Override
    public boolean doesEventExistByUserIdAndEventId(long userId, long eventId) {
        LOG.info("Retrieving tickets by user id {} and event id {}...", userId, eventId);
        return this.tickets
                .values()
                .stream()
                .anyMatch(ticket -> ticket.getUserId() == userId && ticket.getEventId() == eventId);
    }

    @Override
    public boolean deleteById(long ticketId) {
        LOG.info("Deleting a ticket by {} id...", ticketId);
        if (!tickets.containsKey(ticketId)) {
            LOG.warn("No ticket was found with such id");
            return false;
        }
        tickets.remove(ticketId);
        LOG.info("The ticket was deleted successfully");
        return true;
    }

    private List<Ticket> filter(Predicate<Ticket> predicate, int pageSize, int pageNumber) {
        return paginator.paginate(this.tickets
                .values()
                .stream()
                .filter(predicate)
                .toList(), pageSize, pageNumber);
    }
}
