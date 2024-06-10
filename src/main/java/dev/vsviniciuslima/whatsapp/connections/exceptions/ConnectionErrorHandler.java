package dev.vsviniciuslima.whatsapp.connections.exceptions;

import it.auties.whatsapp.api.ClientType;
import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.exception.HmacValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeoutException;

import static it.auties.whatsapp.api.ErrorHandler.Location.*;

@Slf4j
@ApplicationScoped
public class ConnectionErrorHandler implements ErrorHandler {

    @Override
    public Result handleError(Whatsapp whatsapp, Location location, Throwable throwable) {
        if(location == RECONNECT) {
            log.warn("Cannot reconnect: retrying on next timeout");
            return Result.DISCARD;
        }

        if(throwable instanceof CompletionException && throwable.getCause() instanceof TimeoutException) {
            log.warn("Detected possible network anomaly: reconnecting");
            return Result.RECONNECT;
        }

        if(location == LOGIN) {
            return Result.RECONNECT;
        }

        log.error("Socket failure at %s".formatted(location));

        if (location == CRYPTOGRAPHY && whatsapp.store().clientType() == ClientType.MOBILE) {
            log.warn("Reconnecting");
            return Result.RECONNECT;
        }

        if (location == INITIAL_APP_STATE_SYNC
                || location == CRYPTOGRAPHY
                || (location == MESSAGE && throwable instanceof HmacValidationException)) {
            log.warn("Restore");
            return Result.RESTORE;
        }

        if(throwable instanceof SocketException) {
            if (throwable.getMessage().equals("Connection reset") && location == UNKNOWN) {
                log.warn("Could not connect, try again.");
                return Result.DISCARD;
            }
        }

        log.info(throwable.getMessage());
        log.warn("Ignored failure");
        return Result.DISCARD;
    }
}
