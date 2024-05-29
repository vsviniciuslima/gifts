package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.beans.PanacheQuery;
import dev.vsviniciuslima.beans.PanacheQueryBuilder;
import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PageResponse;
import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import dev.vsviniciuslima.gifts.model.Recommendation;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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

    public PageResponse search(PageRequest params) {
        PanacheQuery query = panacheQueryBuilder.buildQuery();
        return Gift.pageResponse(params, query);
    }

    public long count() {
        PanacheQuery query = panacheQueryBuilder.buildQuery();
        return Gift
                .find(query.query(), query.params())
                .count();
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

    public void deleteGift(Long id) {
        log.info("Deleting gift {}", id);
        Gift.deleteById(id);
    }

    public Gift addRecomendations(Long id, Recommendation recommendation) {

        Gift gift = Gift.findById(id);

        if(gift == null) throw new BadRequestException("Gift not found");

        gift.recommendationUrls.add(recommendation.url());
        gift.persist();

        return gift;
    }

    public Gift deleteGiftRecommendations(Long id) {
        Gift gift = Gift.findById(id);
        if(gift == null) throw new BadRequestException("Gift not found");
        gift.recommendationUrls.clear();
        gift.persist();
        return gift;
    }
}
