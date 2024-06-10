package dev.vsviniciuslima.whatsapp.messages;

import dev.vsviniciuslima.whatsapp.connections.exceptions.ConnectionException;
import dev.vsviniciuslima.whatsapp.messages.model.SendMessage;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.controller.Store;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.model.MessageContainer;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessage;
import it.auties.whatsapp.model.message.standard.TextMessageBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@ApplicationScoped
public class WhatsappService {

    public void sendMessage(SendMessage message) {


        Whatsapp whatsapp = Whatsapp.webBuilder()
                .newConnection(message.from())
                .name("Outcome Cloud")
                .registered()
                .orElseThrow(() -> new ConnectionException("O usuário não está registrado", message.from()));

        if(!whatsapp.isConnected()) {
//            throw new ConnectionException("O usuário não está conectado", message.from());
            whatsapp.reconnect();
        }

//        customApi(whatsapp);

        Store store = whatsapp.store();
        store.contacts()
                .stream()
                .filter(contact -> contact.toJid().toPhoneNumber().contains(message.to()))
                .findFirst()
                .ifPresent(contact -> {
                    log.info("enviando mensagem para {}", contact.name());
                    whatsapp.sendMessage(contact.jid(), message.content());
                });
    }

    public void customApi(Whatsapp api) {
        log.info("mandando da custom api tb");
        Whatsapp customApi = Whatsapp.customBuilder()
                .store(api.store())
                .keys(api.keys())
                .errorHandler(new MyErrorHandler())
                .build();

        TextMessage helloWorld = new TextMessageBuilder().text("Hello World").build();
        MessageContainer messageContainer = new MessageContainerBuilder()
                .textMessage(helloWorld)
                .build();

//        Jid jid = api.store().jid().get();
//        MessageInfo join = customApi.sendMessage(jid, messageContainer).join();

        Optional<Jid> jid = api.store().jid();

        if(jid.isEmpty()) return;

        customApi.sendMessage(jid.get(), messageContainer)
                .whenComplete((messageInfo, throwable) -> {
                    log.info("maybe sent message... {}", messageInfo);
                    if(throwable != null) {
                        log.error("Error sending message", throwable);
                    }
                });
    }



}
