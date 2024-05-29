package dev.vsviniciuslima.guests;

import dev.vsviniciuslima.beans.PanacheQueryBuilder;
import dev.vsviniciuslima.beans.PanacheQuery;
import dev.vsviniciuslima.dto.PageRequest;
import dev.vsviniciuslima.dto.PageResponse;
import dev.vsviniciuslima.guests.model.Confirmation;
import dev.vsviniciuslima.guests.model.Guest;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class GuestsService {

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

        for(String guestName : confirmation.guests()) {
            Guest additionalGuest = new Guest();
            additionalGuest.name = guestName;
            additionalGuest.mainGuest = principalGuest.id.toString();
            additionalGuest.createdAt = createdAt;
            additionalGuest.persist();
            log.info("Persisted additional guest: {}", guestName);
        }
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

    public PageResponse search(PageRequest params) {

        PanacheQuery query = panacheQueryBuilder.buildQuery();

        List<Guest> guests = Guest.page(params, query);

        Map<Long, Guest> guestMap = guests.stream()
                .collect(Collectors.toMap(guest -> guest.id, guest -> guest));

        guests = guests.stream()
                .filter(guest -> guest.mainGuest != null)
                .peek(guest -> guest.mainGuest = getMainGuestName(guest.mainGuest, guestMap))
                .toList();

        return new PageResponse(params, guests);
    }

    private String getMainGuestName(String mainGuestIdStr, Map<Long, Guest> guestMap) {
        Long mainGuestId = Long.valueOf(mainGuestIdStr);

        return guestMap.containsKey(mainGuestId) ? guestMap.get(mainGuestId).name
                : Guest.findById(mainGuestId).toString();
    }

    public long countConfirmations() {
        return Guest.count();
    }
}
