package dev.vsviniciuslima.gifts;

import dev.vsviniciuslima.dto.PaginatedResponse;
import dev.vsviniciuslima.gifts.model.BuyGift;
import dev.vsviniciuslima.gifts.model.CreateGift;
import dev.vsviniciuslima.gifts.model.Gift;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ApplicationScoped
public class GiftService {

    @Transactional
    public Gift createGift(CreateGift createGift) {
        log.info("Creating gift {}", createGift);

        Gift gift = new Gift();
        gift.name = createGift.name();
        gift.imageUrl = createGift.imageUrl();
        gift.price = createGift.price();
        gift.category = createGift.category();
        gift.bought = false;
        gift.buyer = null;
        gift.buyerEmail = null;
        gift.buyerMessage = null;
        gift.persist();

        return gift;
    }

    public List<Gift> listAll() {
        log.info("Listing all gifts");
        return Gift.listAll();
    }

    public Gift buyGift(Long id, BuyGift buyGift) {
        log.info("Buying gift {}. \nBuyer info: {}", id, buyGift);

        Gift gift = Gift.findById(id);

        if(gift.bought) throw new RuntimeException("Gift already bought");

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
        List<PanacheEntityBase> gifts = Gift.findAll().page(pageIndex, pageSize).list();
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
}
