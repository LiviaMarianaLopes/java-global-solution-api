package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Repositories.AlertRepository;
import org.example.Repositories.CollaboratorRepository;
import org.example.entities.Alert;
import org.example.services.AlertService;

import java.util.List;

@Path("alert")
public class AlertResource {
    public AlertRepository alertRepository;
    public AlertService alertService;
    public CollaboratorRepository collaboratorRepository;

    public AlertResource() {
        alertRepository = new AlertRepository();
        alertService = new AlertService();
        collaboratorRepository = new CollaboratorRepository();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alert> getAll() {
        return alertRepository.readAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        var user = alertRepository.getAlertById(id);
        return user.isPresent() ?
                Response.ok(user.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Alert alert) {
        try {
            alertService.create(alert);
            return Response.status(Response.Status.CREATED).entity("Cadastro de alerta realizado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @Path("email")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response emailValidate(String email) {
        try {
            var idCollaborator = collaboratorRepository.getId(email);
            if (idCollaborator.isPresent()) {
                return Response.ok(idCollaborator).build();
            } else {
                throw new IllegalArgumentException("Email não cadastrado");
            }
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Alert alert) {
        try {
            alertService.update(id, alert);
            return Response.ok().entity("Dados atualizados com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id) {
        try {
            alertRepository.delete(id);
            return Response.ok().entity("Alerta deletado com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }


}


