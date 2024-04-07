package dev.vsviniciuslima.confirmations;

import dev.vsviniciuslima.beans.PanacheQueryBuilder;
import dev.vsviniciuslima.beans.PanacheQueryData;
import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.confirmations.model.Confirmation;
import dev.vsviniciuslima.confirmations.model.Guest;
import dev.vsviniciuslima.dto.PaginatedResponse;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.lang.StringUtil;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ConfirmationService {

    @Context
    PanacheQueryBuilder panacheQueryBuilder;
    public void confirm(Confirmation confirmation) {
        String principalGuestName = confirmation.principalGuestName();
        LocalDateTime createdAt = LocalDateTime.now();

        Guest principalGuest = new Guest();
        principalGuest.name = principalGuestName;
        principalGuest.createdAt = createdAt;

        principalGuest.persist();
        log.info("Persisted principal guest: {}", principalGuestName);

        confirmation.guests().forEach(guestName -> {
            Guest additionalGuest = new Guest();
            additionalGuest.name = guestName;
            additionalGuest.mainGuest = principalGuest.id.toString();
            additionalGuest.createdAt = createdAt;
            additionalGuest.persist();
            log.info("Persisted additional guest: {}", guestName);
        });
        log.info("Persisted all guests for principal guest {} at {}", principalGuestName, createdAt);
    }

    public List<Confirmation> listConfirmations() {

        List<Guest> allGuests = Guest.listAll(Sort.descending("createdAt"));

        return allGuests.stream()
                .filter(guest -> guest.mainGuest == null)
                .map(mainGuest -> {
                    List<String> additionalGuests = allGuests.stream()
                            .filter(guest -> guest.mainGuest != null && Long.valueOf(guest.mainGuest).equals(mainGuest.id))
                            .map(guest -> guest.name)
                            .toList();

                    return new Confirmation(mainGuest.name, additionalGuests, mainGuest.createdAt);
                })
                .toList();
    }

    public PaginatedResponse search(PageRequest params) {

        PanacheQueryData query = panacheQueryBuilder.buildQuery();
        Page page = params.getPage();

        List<Guest> guests = Guest
                .find(query.query(), params.getSort(), query.params())
                .page(page)
                .list();

        List<Guest> finalGuests = guests;

        guests = guests.stream()
                .filter(guest -> guest.mainGuest != null)
                .peek(additionalGuest -> additionalGuest.mainGuest = finalGuests.stream()
                        .filter(guest -> additionalGuest.mainGuest.equals(String.valueOf(guest.id)))
                        .findFirst()
                        .map(guest -> guest.name)
                        .orElseGet(() -> Guest.findById(Long.valueOf(additionalGuest.mainGuest)).toString()))
                .toList();

        long guestsCount = guests.size();

        return new PaginatedResponse(
                page.index,
                page.size,
                (guestsCount + page.size - 1) / page.size,
                guestsCount,
                params.getSortDirection(),
                params.getSortBy(),
                guests
        );
    }

    public long countConfirmations() {
        return Guest.count();
    }
}
