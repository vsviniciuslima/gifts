package dev.vsviniciuslima.whatsapp.connections;


import dev.vsviniciuslima.whatsapp.connections.model.CreateConnection;
import dev.vsviniciuslima.whatsapp.connections.model.DeleteConnection;
import io.smallrye.common.annotation.Blocking;
import it.auties.whatsapp.api.Whatsapp;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Path("/connections")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "WhatsApp", description = "zapzap")
public class ConnectionsController {

    @Inject
    ConnectionsService connectionsService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createConnection(CreateConnection connection) {
        Whatsapp whatsapp = connectionsService.createConnection(connection);
        return Response.ok(whatsapp, MediaType.TEXT_PLAIN_TYPE).build();
    }

    @POST
    @Path("/qrCode")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createQrCodeConnection(CreateConnection connection) {
        return Response
                .ok(connectionsService.createQrCodeConnection(connection))
                .type("image/png")
                .build();
    }


    @GET
    public Response listConnections() {
        return connectionsService.listConnections();
    }

    @DELETE
    public Response disconnect(DeleteConnection connection) {
        connectionsService.disconnect(connection);
        return Response.ok().build();
    }
}
