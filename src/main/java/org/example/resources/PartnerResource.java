package org.example.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.Repositories.PartnerRepository;
import org.example.Repositories.VolunteerRepository;
import org.example.entities.Partner;
import org.example.entities.Volunteer;
import org.example.services.PartnerService;

import java.util.List;

@Path("partner")

public class PartnerResource {
    public PartnerRepository partnerRepository;
    public PartnerService partnerService;

    public PartnerResource() {
        partnerRepository = new PartnerRepository();
        partnerService = new PartnerService();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Partner> getAll() {
        return partnerRepository.readAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        var partner = partnerRepository.getById(id);
        return partner.isPresent() ?
                Response.ok(partner.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Partner partner) {
        try {
            partnerService.create(partner);
            return Response.status(Response.Status.CREATED).entity("Cadastro de empresa parceira realizado com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Partner partner) {
        try {
            partnerService.update(id, partner);
            return Response.ok().entity("Dados atualizados com sucesso!").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteById(@PathParam("id") int id) {
        try {
            partnerRepository.delete(id);
            return Response.ok().entity("Parceiro deletado com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }


}

