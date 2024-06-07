package org.example.services;

import java.io.IOException;
import java.io.StringReader;


import org.apache.http.client.fluent.Request;
import org.example.Repositories.VolunteerRepository;
import org.example.entities.Volunteer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.util.Objects;

public class VolunteerService {
    private static final String BASE_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=234efca9c32641f9bf2d3737a7fa2c8b&email=";
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
        else {
            if (isValidEmail(volunteer.getEmail())) {
                volunteerRepository.create(volunteer);
            } else {
                throw new IllegalArgumentException("Email inválido");
            }
        }
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
        else {
            if (isValidEmail(volunteer.getEmail())) {
                volunteerRepository.update(id, volunteer);
            } else {
                throw new IllegalArgumentException("Email inválido");
            }
        }
    }


    public boolean isValidEmail(String email) {
        String url = BASE_URL + email;

        try {
            String responseBody = Request.Get(url)
                    .execute()
                    .returnContent()
                    .asString();

            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                String deliverability = jsonObject.getString("deliverability", "");
                boolean isMxFound = jsonObject.getJsonObject("is_mx_found").getBoolean("value", false);
                boolean isValidFormat = jsonObject.getJsonObject("is_valid_format").getBoolean("value", false);
                return "DELIVERABLE".equals(deliverability) && isValidFormat && isMxFound;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
