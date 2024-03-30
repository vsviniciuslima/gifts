package dev.vsviniciuslima.confirmations;

import dev.vsviniciuslima.confirmations.model.Confirmation;
import dev.vsviniciuslima.confirmations.model.Guest;
import dev.vsviniciuslima.gifts.model.ConfirmationsCount;
import dev.vsviniciuslima.gifts.model.Gift;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@ApplicationScoped
public class ConfirmationService {

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

    public long countConfirmations() {
        return Guest.count();
    }
}
