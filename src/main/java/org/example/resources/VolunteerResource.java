package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.example.Repositories.VolunteerRepository;
import org.example.entities.Volunteer;
import org.example.services.VolunteerService;

import java.util.List;

@Path("volunteer")

public class VolunteerResource {
    public VolunteerRepository volunteerRepository;
    public VolunteerService volunteerService;

    public VolunteerResource() {
        volunteerRepository = new VolunteerRepository();
        volunteerService = new VolunteerService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Volunteer> getAll() {
        return volunteerRepository.readAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        var user = volunteerRepository.getVolunteerById(id);
        return user.isPresent() ?
                Response.ok(user.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Volunteer user) {
        try {
            volunteerService.create(user);
            return Response.status(Response.Status.CREATED).entity("Cadastro de voluntário realizado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Volunteer user) {
        try {
            volunteerService.update(id, user);
            return Response.ok().entity("Dados atualizados com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id) {
        try {
            volunteerRepository.delete(id);
            return Response.ok().entity("Voluntário deletado com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }


}

