package dev.vsviniciuslima.whatsapp.connections.listeners;

import dev.vsviniciuslima.whatsapp.connections.model.Connection;
import it.auties.whatsapp.api.DisconnectReason;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.OnWhatsappDisconnected;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
@Transactional
public class DisconnectListener implements OnWhatsappDisconnected {
    @Override
    public void onDisconnected(Whatsapp whatsapp, DisconnectReason reason) {
        if(reason == DisconnectReason.RECONNECTING || reason == DisconnectReason.RESTORE) {
            log.info("discarding...");
            return;
        }

        UUID connectionUuid = whatsapp.store().uuid();

        Connection connection = (Connection) Connection
                .find("uuid", connectionUuid)
                .firstResultOptional()
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        log.info("Removing connection {}", connection);

        connection.active = false;
        connection.updatedAt = LocalDateTime.now();
        connection.persist();

        log.info("Connection was disconnected {}", connection);
    }
}
