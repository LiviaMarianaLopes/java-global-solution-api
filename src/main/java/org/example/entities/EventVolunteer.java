package org.example.entities;

public class EventVolunteer extends _BaseEntity {
    private int idVolunteer;
    private int idEvent;

    public EventVolunteer() {
    }

    public EventVolunteer(int idVolunteer, int idEvent) {
        this.idVolunteer = idVolunteer;
        this.idEvent = idEvent;
    }

    public EventVolunteer(int id, int idVolunteer, int idEvent) {
        super(id);
        this.idVolunteer = idVolunteer;
        this.idEvent = idEvent;
    }

    public int getIdVolnteer() {
        return idVolunteer;
    }

    public void setIdVolnteer(int idVolunteer) {
        this.idVolunteer = idVolunteer;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }
}
