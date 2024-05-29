package dev.vsviniciuslima.guests.model;

import java.time.LocalDateTime;
import java.util.List;

public record GuestConfirmation(String principalGuestName, List<String> guests, LocalDateTime createdAt) {
}
