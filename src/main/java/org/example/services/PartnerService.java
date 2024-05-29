package org.example.services;

import org.example.Repositories.PartnerRepository;
import org.example.entities.Partner;

import java.util.Objects;

public class PartnerService {
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
        else
            partnerRepository.create(partner);
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
        else
            partnerRepository.update(id, partner);
    }
}
