package org.example.services;

import org.example.Repositories.EventsVolunteersRepository;
import org.example.Repositories.VolunteerRepository;
import org.example.entities.EventVolunteer;

public class EventsVolunteersService {
    EventsVolunteersRepository eventsVolunteersRepository;
    VolunteerRepository volunteerRepository;
    public EventsVolunteersService(){
        eventsVolunteersRepository = new EventsVolunteersRepository();
        volunteerRepository = new VolunteerRepository();
    }
    public void registerVolunteerInEvent(int idEvent, String email) {
        int idVolunteer = volunteerRepository.getIdVolunteer(email);
        var eventVolunteer = new EventVolunteer(idVolunteer, idEvent);
        if(idVolunteer != 0) {
            eventsVolunteersRepository.registerVolunteerInEvent(eventVolunteer);
        }
        else{
            throw new IllegalArgumentException("Email não cadastrado");
        }
    }
}
