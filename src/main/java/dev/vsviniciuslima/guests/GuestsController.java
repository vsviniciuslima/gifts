package dev.vsviniciuslima.guests;


import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PageResponse;
import dev.vsviniciuslima.guests.model.Confirmation;
import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.guests.model.Guest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Slf4j
@Path("/guests")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Convidados", description = "Buscar e gerenciar convidados")
public class GuestsController {

    @Inject
    GuestsService service;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response confirm(Confirmation confirmation) {
        service.confirm(confirmation);
        return Response.ok().build();
    }

    @GET
    public Response getConfirmations() {
        return Response.ok(service.listConfirmations()).build();
    }

    @GET
    @Path("/guests")
    @Operation(summary = "Listar confirmados", description = "Listar os convidados confirmados")
    public Response getGuests(@BeanParam PageRequest params ) {
        PageResponse search = service.search(params);
        return Response.ok(search).build();
    }

    @GET
    @Path("/count")
    public Response countConfirmations() {
        return Response.ok(service.countConfirmations()).build();
    }

}
