package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.beans.PanacheQuery;
import dev.vsviniciuslima.beans.PanacheQueryBuilder;
import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.gifts.model.Recommendation;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ApplicationScoped
public class GiftService {

    @Context
    UriInfo uriInfo;

    @Context
    PanacheQueryBuilder panacheQueryBuilder;

    @Transactional
    public Gift createGift(CreateGift createGift) {
        log.info("Creating gift {}", createGift);

        Gift gift = new Gift();
        gift.name = createGift.name();
        gift.imageUrl = createGift.imageUrl();
        gift.price = createGift.price();
        gift.category = createGift.category();

        if(createGift.recommendations() != null) {
            gift.recommendationUrls = createGift.recommendations().stream().map(Recommendation::url).toList();
        }

        gift.bought = false;
        gift.buyer = null;
        gift.buyerEmail = null;
        gift.buyerMessage = null;
        gift.persist();

        log.info("Succesfully created!");
        return gift;
    }

    public List<Gift> search() {
        Optional<PanacheQuery> query = panacheQueryBuilder.buildQuery();

        if(query.isEmpty()) return Gift.listAll();

        return Gift
                .find(query.get().query(), query.get().params())
                .list();
    }

    public List<Gift> listAll() {
        log.info("Listing all gifts");
        return Gift.listAll();
    }

    public Gift buyGift(Long id, BuyGift buyGift) {
        log.info("Buying gift {}. \nBuyer info: {}", id, buyGift);

        Gift gift = Gift.findById(id);

        if(gift.bought) throw new BadRequestException("Gift already bought");

        gift.bought = true;
        gift.buyer = buyGift.buyer();
        gift.buyerEmail = buyGift.buyerEmail();
        gift.buyerMessage = buyGift.buyerMessage();
        gift.persist();

        return gift;
    }

    public List<Gift> listBoughtGifts() {
        return Gift.listBoughtGifts();
    }

    public void deleteGift(Long id) {
        log.info("Deleting gift {}", id);
        Gift.deleteById(id);
    }

    public PaginatedResponse listPaged(int pageIndex, int pageSize) {

        List<PanacheEntityBase> gifts = Gift
                .find("bought", false)
                .page(pageIndex, pageSize)
                .list();

        long count = Gift.count();
        long totalPages = (count + pageSize - 1) / pageSize;

        return new PaginatedResponse(
                pageIndex,
                pageSize,
                totalPages,
                count,
                gifts
        );
    }

    public Gift addRecomendations(Long id, Recommendation recommendation) {

        Gift gift = Gift.findById(id);

        if(gift == null) throw new BadRequestException("Gift not found");

        gift.recommendationUrls.add(recommendation.url());
        gift.persist();

        return gift;
    }
}
