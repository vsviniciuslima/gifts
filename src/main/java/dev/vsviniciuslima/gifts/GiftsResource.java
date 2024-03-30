package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.Recommendation;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Path("/gifts")
@Produces(MediaType.APPLICATION_JSON)
public class GiftsResource {

    @Inject
    GiftService giftService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createGift(CreateGift createGift) {
        Gift gift = giftService.createGift(createGift);
        URI location;

        try {
            location = new URI("/gifts/" + gift.id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return Response
                .created(location)
                .entity(gift)
                .build();
    }

    @POST
    @Path("/bulk")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bulkCreateGifts(List<CreateGift> createGifts) {
        for (CreateGift createGift : createGifts) {
            giftService.createGift(createGift);
        }
        return Response.noContent().build();
    }

    @GET
    public Response getGifts() {
        return Response.ok(giftService.search()).build();
    }

    @GET
    @Path("/byName")
    public Response getByName(@QueryParam("name") String name) {
        PanacheEntityBase gift = Gift.find("name", name)
                .firstResultOptional()
                .orElseThrow(() -> new BadRequestException("Gift not found"));
        return Response.ok(gift).build();
    }

    @GET
    @Path("/count")
    public Response countGifts() {
        return Response.ok(Gift.find("bought", true).count()).build();
    }

    @GET
    @Path("/countAvailable")
    public Response countNotBought() {
        return Response.ok(Gift.find("bought", false).count()).build();
    }



    @POST
    @Transactional
    @Path("/{id}/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyGift(@PathParam("id") Long id, BuyGift buyGift) {
        Gift gift = giftService.buyGift(id, buyGift);
        return Response.ok(gift).build();
    }

    @POST
    @Transactional
    @Path("/{id}/recommendations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRecomendations(@PathParam("id") Long id, Recommendation recommendation) {
        Gift gift = giftService.addRecomendations(id, recommendation);
        return Response.ok(gift).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}/recommendations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteRecommendations(@PathParam("id") Long id) {
        Gift gift = Gift.findById(id);
        if(gift == null) throw new BadRequestException("Gift not found");
        gift.recommendationUrls.clear();
        gift.persist();
        return Response.ok(gift).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteGift(@PathParam("id") Long id) {
        giftService.deleteGift(id);
        return Response.noContent().build();
    }

    @DELETE
    @Transactional
    public Response deleteAll() {
        Gift.deleteAll();
        return Response.noContent().build();
    }

    @GET
    @Path("/paginated")
    public Response getGiftsPaged(@QueryParam("page") int page, @QueryParam("size") int size) {
        return Response.ok(giftService.listPaged(page, size)).build();
    }
}
