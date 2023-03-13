package com.epam.spring.dao.impl;

import com.epam.spring.annotation.BindStaticData;
import com.epam.spring.dao.EventDao;
import com.epam.spring.model.Event;
import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.util.IdGenerator;
import com.epam.spring.util.Paginator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Setter
public class EventDaoImpl implements EventDao {
    @BindStaticData(fileLocation = "preparedEvents.json", castTo = EventEntity.class)
    private final Map<Long, Event> events = new HashMap<>();
    private Paginator paginator;
    private IdGenerator idGenerator;

    @Override
    public Optional<Event> getEventById(long eventId) {
        LOG.info("Retrieving an event by {} id...", eventId);
        return Optional.ofNullable(events.get(eventId));
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOG.debug("Retrieving events by {}... Passed page size - {}, page number - {}", title, pageSize, pageNum);
        return filter(event -> event.getTitle().equals(title), pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOG.info("Retrieving events by {}... Passed page size - {}, page number - {}", day, pageSize, pageNum);
        return filter(event -> event.getDate().equals(day), pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        event.setId(idGenerator.generateId(EventEntity.class));
        LOG.info("Adding a new event: title - {}, date - {}...", event.getTitle(), event.getDate());
        events.put(event.getId(), event);
        LOG.info("The event was added successfully");
        return events.get(event.getId());
    }

    @Override
    public Optional<Event> updateEvent(Event event) {
        if (events.containsKey(event.getId())) {
            LOG.info("Updating an event by {} id with the following data: title - {}, date - {}...",
                    event.getId(), event.getTitle(), event.getDate());
            events.put(event.getId(), event);
            LOG.info("The event was updated successfully");
            return Optional.of(events.get(event.getId()));
        }
        LOG.warn("Such event was not found while updating");
        return Optional.empty();
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOG.info("Deleting an event by {} id...", eventId);
        if (!events.containsKey(eventId)) {
            LOG.warn("No event was found with such id");
            return false;
        }
        events.remove(eventId);
        LOG.info("The event was deleted successfully");
        return true;
    }

    private List<Event> filter(Predicate<Event> predicate, int pageSize, int pageNumber) {
        return paginator.paginate(events
                .values()
                .stream()
                .filter(predicate)
                .toList(), pageSize, pageNumber);
    }
}
