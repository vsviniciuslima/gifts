package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PageResponse;
import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.Recommendation;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Slf4j
@Path("/gifts")
@Produces(MediaType.APPLICATION_JSON)
public class GiftsResource {

    @Inject
    GiftService giftService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Criar presentes")
    @APIResponse(description = "Lista de presentes criados",
            responseCode = "200",
            content = { @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = Gift.class,
                            type = SchemaType.ARRAY
                    ))
            })
    @Tag(name = "Gerenciar presentes", description = "Adicionar, remover e atualizar presentes")
    public Response createGifts(
            @RequestBody(description = "Lista de presentes a serem criados") List<CreateGift> createGifts
    ) {
        List<Gift> createdGifts = createGifts.stream()
                .map(request -> giftService.createGift(request))
                .toList();

        return Response.ok(createdGifts).build();
    }

    @GET
    @Operation(summary = "Listar presentes",
            description = "Listar todos os presentes cadastrados")
    @Tag(name = "Buscar presentes", description = "Listar e filtar presentes")
    public Response getGifts( @BeanParam PageRequest params ) {
        PageResponse search = giftService.search(params);
        return Response.ok(search).build();
    }

    @GET
    @Tag(name = "Buscar presentes", description = "Listar e filtar presentes")
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return Response.ok(giftService.count()).build();
    }


    @POST
    @Transactional
    @Path("/{id}/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = "Gerenciar presentes", description = "Adicionar, remover e atualizar presentes")
    public Response buyGift(@PathParam("id") Long id, BuyGift buyGift) {
        Gift gift = giftService.buyGift(id, buyGift);
        return Response.ok(gift).build();
    }

    @POST
    @Transactional
    @Path("/{id}/recommendations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = "Gerenciar presentes", description = "Adicionar, remover e atualizar presentes")
    public Response addRecomendations(@PathParam("id") Long id, Recommendation recommendation) {
        Gift gift = giftService.addRecomendations(id, recommendation);
        return Response.ok(gift).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}/recommendations")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tag(name = "Gerenciar presentes", description = "Adicionar, remover e atualizar presentes")
    public Response deleteRecommendations(@PathParam("id") Long id) {
        return Response.ok(giftService.deleteGiftRecommendations(id)).build();
    }



    @DELETE
    @Transactional
    @Path("/{id}")
    @Tag(name = "Gerenciar presentes", description = "Adicionar, remover e atualizar presentes")
    public Response deleteGift(@PathParam("id") Long id) {
        giftService.deleteGift(id);
        return Response.noContent().build();
    }
}
