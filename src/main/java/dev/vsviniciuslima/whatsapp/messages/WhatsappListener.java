package dev.vsviniciuslima.whatsapp.messages;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.Listener;
import it.auties.whatsapp.listener.RegisterListener;
import it.auties.whatsapp.model.info.MessageInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RegisterListener
public record WhatsappListener(Whatsapp whatsapp) implements Listener {
    @Override
    public void onNewMessage(MessageInfo info) {
        info.message()
                .textMessage()
                .ifPresent(textMessage -> log.info("New message from {}: {}", info.senderJid(), textMessage.text()));
    }

}
