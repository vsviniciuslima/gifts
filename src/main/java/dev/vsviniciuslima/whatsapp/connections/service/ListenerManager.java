package dev.vsviniciuslima.whatsapp.connections.service;

import dev.vsviniciuslima.whatsapp.connections.listeners.DisconnectListener;
import dev.vsviniciuslima.whatsapp.connections.listeners.LoggedInListener;
import dev.vsviniciuslima.whatsapp.connections.listeners.MessageListener;
import it.auties.whatsapp.api.Whatsapp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ListenerManager {

    @Inject
    LoggedInListener loggedInListener;

    @Inject
    DisconnectListener disconnectListener;

    @Inject
    MessageListener messageListener;

    public Whatsapp addListeners(Whatsapp whatsapp) {
        return whatsapp
                .addLoggedInListener(loggedInListener)
                .addDisconnectedListener(disconnectListener)
                .addNewChatMessageListener(messageListener);
    }
}
