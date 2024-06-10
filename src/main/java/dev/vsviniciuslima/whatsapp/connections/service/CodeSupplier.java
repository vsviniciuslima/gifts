package dev.vsviniciuslima.whatsapp.connections.service;

import io.vertx.mutiny.core.eventbus.EventBus;
import it.auties.whatsapp.api.AsyncVerificationCodeSupplier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class CodeSupplier implements AsyncVerificationCodeSupplier {

    @Inject
    EventBus eventBus;

    public long phoneNumber;

    @Override
    public CompletableFuture<String> get() {
        return null;
    }

    public CodeSupplier setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

}
