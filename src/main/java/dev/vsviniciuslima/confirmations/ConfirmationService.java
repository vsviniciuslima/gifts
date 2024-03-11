package dev.vsviniciuslima.confirmations;

import dev.vsviniciuslima.confirmations.model.Confirmation;
import dev.vsviniciuslima.confirmations.model.Guest;
import dev.vsviniciuslima.gifts.model.ConfirmationsCount;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ConfirmationService {

    public void confirm(Confirmation confirmation) {
        String principalGuestName = confirmation.principalGuestName();

        Guest.find("name", principalGuestName)
                .firstResultOptional()
                .ifPresent(guest -> {
                    throw new RuntimeException("Guest already confirmed: " + principalGuestName);
                });
        
        LocalDateTime createdAt = LocalDateTime.now();

        Guest principalGuest = new Guest();
        principalGuest.name = principalGuestName;
        principalGuest.createdAt = createdAt;

        principalGuest.persist();
        log.info("Persisted principal guest: {}", principalGuestName);

        confirmation.guests().forEach(guestName -> {
            Guest additionalGuest = new Guest();
            additionalGuest.name = guestName;
            additionalGuest.mainGuest = principalGuestName;
            additionalGuest.createdAt = createdAt;
            additionalGuest.persist();
            log.info("Persisted additional guest: {}", guestName);
        });
        log.info("Persisted all guests for principal guest {} at {}", principalGuestName, createdAt);
    }

    public List<Confirmation> listConfirmations() {
        List<Guest> mainGuests = Guest.list("mainGuest is null");

        return mainGuests.stream().map(mainGuest -> {
            List<String> additionalGuestsNames = Guest
                    .list("mainGuest", mainGuest.name)
                    .stream()
                    .map(guest -> ((Guest) guest).name)
                    .toList();

            return new Confirmation(mainGuest.name, additionalGuestsNames);
        }).toList();
    }

    public ConfirmationsCount countConfirmations() {
        return new ConfirmationsCount(Guest.count());
    }
}
