package org.example.services;

import org.example.Repositories.AlertRepository;
import org.example.Repositories.CollaboratorRepository;
import org.example.entities.Alert;
import org.example.entities.Collaborator;

public class AlertService {
    private AlertRepository alertRepository;
    private CollaboratorRepository collaboratorRepository;

    public AlertService() {
        alertRepository = new AlertRepository();
        collaboratorRepository = new CollaboratorRepository();
    }

    public void create(Alert alert) {
        if (idCollaboratorValidate(alert)) {
            var validation = alert.validate();
            if (validation.containsKey(false))
                throw new IllegalArgumentException(validation.get(false).toString());
            else
                alertRepository.create(alert);
        } else {
            throw new IllegalArgumentException("ID do colaborador inválido");
        }
    }


    public void update(int id, Alert alert) {

        if (idCollaboratorValidate(alert)) {
            var validation = alert.validate();
            if (validation.containsKey(false))
                throw new IllegalArgumentException(validation.get(false).toString());
            else
                alertRepository.update(id, alert);
        } else {
            throw new IllegalArgumentException("ID do colaborador inválido");
        }
    }
    public boolean idCollaboratorValidate(Alert alert){
        var collaborators = collaboratorRepository.readAll();
        for (Collaborator c : collaborators) {
            if (c.getId() == alert.getIdColaborador()) {
                return true;
            }
        }
        return false;
    }
}