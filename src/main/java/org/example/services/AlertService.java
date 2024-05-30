package org.example.services;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.example.Repositories.AlertRepository;
import org.example.Repositories.CollaboratorRepository;
import org.example.entities.Alert;
import org.example.entities.Collaborator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AlertService {
    private AlertRepository alertRepository;
    private CollaboratorRepository collaboratorRepository;
    private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";

    public AlertService() {
        alertRepository = new AlertRepository();
        collaboratorRepository = new CollaboratorRepository();
    }

    public void create(Alert alert) throws IOException, InterruptedException {

        if (idCollaboratorValidate(alert)) {
            cepValidate(alert.getCep());
            var validation = alert.validate();
            if (validation.containsKey(false))
                throw new IllegalArgumentException(validation.get(false).toString());
            else
                alertRepository.create(alert);
        } else {
            throw new IllegalArgumentException("ID do colaborador inválido");
        }
    }


    public void update(int id, Alert alert) throws IOException, InterruptedException {

        if (idCollaboratorValidate(alert)) {
            cepValidate(alert.getCep());
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

    public  boolean cepValidate(String cep) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(VIACEP_URL, cep)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new IllegalArgumentException("CEP inválido");
        }
    }
}