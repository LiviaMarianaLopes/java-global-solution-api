package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Repositories.EventsVolunteersRepository;
import org.example.entities.EventVolunteer;
import org.example.entities.Partner;
import org.example.services.EventsVolunteersService;

import java.util.List;

@Path("eventsvolunteers")
public class EventsVolunteersResource {
    EventsVolunteersService eventsVolunteersService;
    EventsVolunteersRepository eventsVolunteersRepository;
    public EventsVolunteersResource() {
        this.eventsVolunteersService = new EventsVolunteersService();
        this.eventsVolunteersRepository = new EventsVolunteersRepository();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventVolunteer> getAll() {
        return eventsVolunteersRepository.readAll();
    }
    @POST
    @Path("{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response create(@PathParam("id") int id, String email) {
        try {
            System.out.println(email);
            eventsVolunteersService.registerVolunteerInEvent(id, email);
            return Response.status(Response.Status.CREATED).entity("Cadastro de participação realizado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
