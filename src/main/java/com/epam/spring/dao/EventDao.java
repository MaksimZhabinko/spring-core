package com.epam.spring.dao;

import com.epam.spring.model.Event;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    Optional<Event> getEventById(long eventId);
    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);
    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);
    Event createEvent(Event event);
    Optional<Event> updateEvent(Event event);
    boolean deleteEvent(long eventId);
}
