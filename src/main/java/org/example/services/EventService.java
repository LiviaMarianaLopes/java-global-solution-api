package org.example.services;

import org.example.Repositories.EventRepository;
import org.example.entities.Event;

import java.util.Objects;

public class EventService {
    private EventRepository eventRepository;

    public EventService() {
        eventRepository = new EventRepository();
    }

    public void create(Event event) {

        var validation = event.validate();
        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            eventRepository.create(event);
    }

    public void update(int id, Event event) {

        var validation = event.validate();

        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            eventRepository.update(id, event);
    }
}
