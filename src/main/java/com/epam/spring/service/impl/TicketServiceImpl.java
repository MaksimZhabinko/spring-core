package com.epam.spring.service.impl;

import com.epam.spring.dao.EventDao;
import com.epam.spring.dao.TicketDao;
import com.epam.spring.dao.UserDao;
import com.epam.spring.model.Event;
import com.epam.spring.model.Ticket;
import com.epam.spring.model.User;
import com.epam.spring.model.entity.TicketEntity;
import com.epam.spring.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketDao ticketDao;
    private final UserDao userDao;
    private final EventDao eventDao;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        if (userDao.getUserById(userId).isPresent() && eventDao.getEventById(eventId).isPresent()) {
            if (ticketDao.doesEventExistByUserIdAndEventId(userId, eventId)) {
                LOG.warn("There is already an event with passed ids(userId - {}, eventId - {}) added", userId, eventId);
                throw new IllegalStateException();
            }
            Ticket ticket = new TicketEntity(userId, eventId, category, place);
            return ticketDao.createTicket(ticket);
        }
        LOG.warn("Either a user or an event does not exist with the passed ids: userId - {}, eventId - {}", userId, eventId);
        return null;
    }

    @Override
    public List<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum) {
        return ticketDao
                .getBookedTicketsByUser(user, pageSize, pageNum)
                .stream()
                .map(ticket -> ImmutablePair.of(eventDao.getEventById(ticket.getEventId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getDate(), Comparator.reverseOrder()))
                .map(Pair::getRight)
                .toList();
    }

    @Override
    public List<Ticket> getBookedTicketsByEvent(Event event, int pageSize, int pageNum) {
        return ticketDao
                .getBookedTicketsByEvent(event, pageSize, pageNum)
                .stream()
                .map(ticket -> ImmutablePair.of(userDao.getUserById(ticket.getUserId()).get(), ticket))
                .sorted(Comparator.comparing(pair -> pair.getLeft().getEmail()))
                .map(Pair::getRight)
                .toList();
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketDao.deleteById(ticketId);
    }
}
