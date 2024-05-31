package org.example.services;

import org.apache.http.client.fluent.Request;
import org.example.Repositories.PartnerRepository;
import org.example.entities.Partner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;

public class PartnerService {
    private static final String BASE_URL = "https://emailvalidation.abstractapi.com/v1/?api_key=6b82ca468eb240b5b5af38a2b24a642e&email=";
    private PartnerRepository partnerRepository;

    public PartnerService() {
        partnerRepository = new PartnerRepository();
    }

    public void create(Partner partner) {

        var partners = partnerRepository.readAll();
        for (Partner v : partners) {
            if (Objects.equals(v.getEmail(), partner.getEmail())) {
                throw new IllegalArgumentException("Este email já foi cadastrado");

            }

        }
        var validation = partner.validate();
        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else {
            if (isValidEmail(partner.getEmail())) {
                partnerRepository.create(partner);
            } else {
                throw new IllegalArgumentException("Email inválido");
            }
        }
    }

    public void update(int id, Partner partner) {
        var partners = partnerRepository.readAll();
        for (Partner v : partners) {
            if (Objects.equals(v.getEmail(), partner.getEmail())) {
                if (v.getId() != id) {
                    throw new IllegalArgumentException("Email inválido");
                }
            }

        }
        var validation = partner.validate();

        if (validation.containsKey(false))
            throw new IllegalArgumentException(validation.get(false).toString());
        else {
            if (isValidEmail(partner.getEmail())) {
            partnerRepository.update(id, partner);
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
