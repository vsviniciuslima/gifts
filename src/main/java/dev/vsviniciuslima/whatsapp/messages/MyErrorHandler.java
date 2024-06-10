package dev.vsviniciuslima.whatsapp.messages;

import it.auties.whatsapp.api.ErrorHandler;
import it.auties.whatsapp.api.Whatsapp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyErrorHandler implements ErrorHandler {

    @Override
    public Result handleError(Whatsapp whatsapp, Location location, Throwable throwable) {
        log.error("Error handling message", throwable);
        log.error("Location: {}", location);
        log.error("Whatsapp: {}", whatsapp);
        return null;
    }
}
