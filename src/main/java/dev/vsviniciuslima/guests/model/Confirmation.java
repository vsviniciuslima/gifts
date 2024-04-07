package dev.vsviniciuslima.confirmations.model;

import java.time.LocalDateTime;
import java.util.List;

public record Confirmation(
        String principalGuestName,
        List<String> guests,
        LocalDateTime createdAt
) { }
