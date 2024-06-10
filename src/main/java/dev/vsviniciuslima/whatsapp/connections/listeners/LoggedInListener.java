package dev.vsviniciuslima.whatsapp.connections.listeners;

import dev.vsviniciuslima.whatsapp.connections.model.Connection;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.listener.OnWhatsappLoggedIn;
import it.auties.whatsapp.listener.RegisterListener;
import it.auties.whatsapp.model.business.BusinessCategory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RegisterListener
@ApplicationScoped
public class LoggedInListener implements OnWhatsappLoggedIn {
    @Override
    @Transactional
    public void onLoggedIn(Whatsapp api) {
        log.info("Logging in...");
        Connection connection = new Connection();


        Store userData = api.store();
        userData.businessCategory().ifPresent(businessCategory -> {
            api.queryBusinessCatalog().whenComplete((catalog, throwable) -> {
                if(throwable != null){
                    log.error("Failed to query business catalog", throwable);
                    return;
                }

                catalog.forEach(catalogItem -> log.info("Catalog item: {}", catalogItem));
            });
        });

        userData.phoneNumber()
                .ifPresent(phoneNumber -> connection.phoneNumber = phoneNumber.number());
        userData.jid()
                .ifPresent(jid -> connection.jid = jid.toPhoneNumber());
        connection.uuid = userData.uuid();
        connection.name = userData.name();

        log.info("Logged in with {}", connection);
        connection.persist();
    }
}
