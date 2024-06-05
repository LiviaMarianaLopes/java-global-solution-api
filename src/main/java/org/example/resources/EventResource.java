package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Repositories.EventRepository;
import org.example.entities.Event;
import org.example.services.EventService;

import java.util.List;

@Path("event")
public class EventResource {
    public EventRepository eventRepository;
    public EventService eventService;

    public EventResource() {
        eventRepository = new EventRepository();
        eventService = new EventService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Event> getAll(@QueryParam("orderby") String orderBy) {
        return eventRepository.readAll(orderBy);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        var event = eventRepository.getById(id);
        return event.isPresent() ?
                Response.ok(event.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Event event) {
        try {
            eventService.create(event);
            return Response.status(Response.Status.CREATED).entity("Evento cadastrado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Event event) {
        try {
            eventService.update(id, event);
            return Response.ok().entity("Dados atualizados com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id) {
        try {
            eventRepository.delete(id);
            return Response.ok().entity("Evento deletado com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

}


