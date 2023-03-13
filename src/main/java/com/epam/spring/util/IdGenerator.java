package com.epam.spring.util;

import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.model.entity.TicketEntity;
import com.epam.spring.model.entity.UserEntity;
import lombok.Setter;

@Setter
public class IdGenerator {
    private static long USER_COUNTER = 1;
    private static long TICKET_COUNTER = 1;
    private static long EVENT_COUNTER = 1;

    public long generateId(Class<?> clazz) {
        if (clazz.equals(UserEntity.class)) {
            return USER_COUNTER++;
        }
        if (clazz.equals(TicketEntity.class)) {
            return TICKET_COUNTER++;
        }
        if (clazz.equals(EventEntity.class)) {
            return EVENT_COUNTER++;
        }
        return 0;
    }
}
