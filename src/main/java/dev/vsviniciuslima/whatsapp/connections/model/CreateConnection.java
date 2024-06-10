package dev.vsviniciuslima.whatsapp.connections.model;

import java.util.Optional;

public record CreateConnection(Optional<Long> phoneNumber, ConnectionMode connectionMode) {}
