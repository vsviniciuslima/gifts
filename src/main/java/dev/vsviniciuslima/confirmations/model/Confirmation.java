package dev.vsviniciuslima.confirmations.model;

import java.util.List;
import java.util.Optional;

public record Confirmation(
        String principalGuestName,
        List<String> guests
) { }
