package com.epam.spring.util;

import com.epam.spring.model.entity.EventEntity;
import com.epam.spring.model.entity.TicketEntity;
import com.epam.spring.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@ExtendWith(MockitoExtension.class)
public class IdGeneratorTest {
    @InjectMocks
    private IdGenerator sut;

    @Test
    void shouldGenerateIdWhenDifferentEntitiesPassed() {
        //given
        Class<UserEntity> userClass = UserEntity.class;
        Class<TicketEntity> ticketClass = TicketEntity.class;
        Class<EventEntity> eventClass = EventEntity.class;

        //when
        long userId = sut.generateId(userClass);
        long eventId = sut.generateId(eventClass);
        long ticketId = sut.generateId(ticketClass);

        //then
        assertThat(userId, is(6L));
        assertThat(eventId, is(6L));
        assertThat(ticketId, is(7L));
    }
}
