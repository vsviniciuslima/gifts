package dev.vsviniciuslima.whatsapp.messages;

import dev.vsviniciuslima.whatsapp.messages.model.SendMessage;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "WhatsApp", description = "zapzap")
public class WhatsAppController {

    @Inject
    WhatsappService service;

    @POST
    @Path("/messages")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(SendMessage message) {
//        try {

        service.sendMessage(message);
//        } catch (Exception e) {
//            log.error("Error sending message", e);
//            return Response.serverError().build();
//        }
        return Response.ok().build();
    }

}
