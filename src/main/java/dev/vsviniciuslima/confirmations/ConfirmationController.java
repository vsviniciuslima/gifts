package dev.vsviniciuslima.confirmations;


import dev.vsviniciuslima.confirmations.model.Confirmation;
import dev.vsviniciuslima.confirmations.model.Guest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/confirmations")
@Produces(MediaType.APPLICATION_JSON)
public class ConfirmationController {

    @Inject
    ConfirmationService service;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirm(Confirmation confirmation) {
        log.info("Confirming {}", confirmation);
        service.confirm(confirmation);
        return Response.ok().build();
    }

    @GET
    public Response getConfirmations() {
        return Response.ok(service.listConfirmations()).build();
    }

    @GET
    @Path("/count")
    public Response countConfirmations() {
        return Response.ok(service.countConfirmations()).build();
    }
    
    @DELETE
    @Transactional
    @Path("/clear")
    public void clear() {
        Guest.deleteAll();
    }

}
