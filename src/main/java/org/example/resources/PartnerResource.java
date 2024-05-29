package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Repositories.PartnerRepository;
import org.example.Repositories.VolunteerRepository;
import org.example.entities.Partner;
import org.example.entities.Volunteer;

import java.util.List;

@Path("partner")

public class PartnerResource {
    public PartnerRepository userRepositoryOrcl;

    public PartnerResource() {
        userRepositoryOrcl = new PartnerRepository();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Partner> getAll() {
        return userRepositoryOrcl.readAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        var user = userRepositoryOrcl.getPartenerById(id);
        return user.isPresent() ?
                Response.ok(user.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Partner user) {
        try {
            userRepositoryOrcl.create(user);
            return Response.status(Response.Status.CREATED).entity("Cadastro de empresa parceira realizado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Partner user) {
        try {
            userRepositoryOrcl.update(id, user);
            return Response.ok().entity("Dados atualizados com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id) {
        try {
            userRepositoryOrcl.delete(id);
            return Response.ok().entity("Parceiro deletado com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }


}

