package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.GiftService;
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
@ApplicationPath("/api")
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
    public Response getGifts() throws InterruptedException {
        return Response.ok(giftService.listAll()).build();
    }

    @GET
    @Path("/bought")
    public Response getBoughtGifts() {
        return Response.ok(giftService.listBoughtGifts()).build();
    }

    @POST
    @Transactional
    @Path("/{id}/buy")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyGift(@PathParam("id") Long id, BuyGift buyGift) throws InterruptedException {
        Gift gift = giftService.buyGift(id, buyGift);
        return Response.ok(gift).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteGift(@PathParam("id") Long id) {
        giftService.deleteGift(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/paginated")
    public Response getGiftsPaged(@QueryParam("page") int page, @QueryParam("size") int size) {
        return Response.ok(giftService.listPaged(page, size)).build();
    }
}
