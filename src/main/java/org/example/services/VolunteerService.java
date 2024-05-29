package org.example.services;

import org.example.Repositories.VolunteerRepository;
import org.example.entities.Volunteer;

import java.util.Objects;

public class VolunteerService {
    private VolunteerRepository volunteerRepository;

    public VolunteerService() {
        volunteerRepository = new VolunteerRepository();
    }

    public void create(Volunteer volunteer) {

        var volunteers = volunteerRepository.readAll();
        for (Volunteer v : volunteers) {
            if (Objects.equals(v.getEmail(), volunteer.getEmail())) {
                throw new IllegalArgumentException("Este email já foi cadastrado");

            }

        }
        var validation = volunteer.validate();
        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            volunteerRepository.create(volunteer);
    }


    public void update(int id, Volunteer volunteer) {
        var volunteers = volunteerRepository.readAll();
        for (Volunteer v : volunteers) {
            if (Objects.equals(v.getEmail(), volunteer.getEmail())) {
                if (v.getId() != id) {
                    throw new IllegalArgumentException("Email inválido");
                }
            }

        }
        var validation = volunteer.validate();

        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else
            volunteerRepository.update(id, volunteer);
    }
}
