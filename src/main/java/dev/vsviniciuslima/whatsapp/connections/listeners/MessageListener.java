package dev.vsviniciuslima.whatsapp.connections.listeners;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.listener.OnWhatsappNewMessage;
import it.auties.whatsapp.model.chat.Chat;
import it.auties.whatsapp.model.contact.Contact;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.info.MessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Optional;


@Slf4j
@ApplicationScoped
public class MessageListener implements OnWhatsappNewMessage {

    @ConfigProperty(name = "whatsapp.allowedNumbers")
    List<String> allowedNumbers;

    private static final String BOT_NAME = "Outcome Cloud";

    @Override
    public void onNewMessage(Whatsapp whatsappApi, MessageInfo messageInfo) {

        Jid parentJid = messageInfo.parentJid();
        Jid senderJid = messageInfo.senderJid();

        if (!shouldReply(whatsappApi, messageInfo, senderJid)) return;

        String clientName = whatsappApi.store()
                .findContactByJid(senderJid)
                .map(Contact::name)
                .map(name -> " " + name)
                .orElse("");

        String replyMessage = String.format(
        """
        *%s*
        Olá%s, como posso ajudar chefe?
        """, BOT_NAME, clientName);

        log.info("Enviando mensagem de {} para {}", parentJid, senderJid);

        whatsappApi.sendMessage(senderJid, replyMessage).whenComplete((sentMessageInfo, throwable) -> {
            if(throwable != null) {
                log.error("Failed to send message", throwable);
                return;
            }

            whatsappApi.awaitMessageReply((ChatMessageInfo) sentMessageInfo)
                    .whenComplete((chatMessageInfo, throwable1) -> {
                        if(throwable1 != null) {
                            log.error("Failed to await message reply", throwable1);
                            return;
                        }

                        log.info("Resposta recebida: {}", chatMessageInfo.message().textMessage());
                    });

            log.info("Mensagem enviada com sucesso: {}", sentMessageInfo);
        });

//        whatsappApi.awaitMessageReply((ChatMessageInfo) messageInfo)
//                .whenComplete((chatMessageInfo, throwable) -> {
//                    if(throwable != null) {
//                        log.error("Failed to await message reply", throwable);
//                        return;
//                    }
//
//                    log.info("Resposta recebida: {}", chatMessageInfo.message().textMessage());
//                });

        Optional<String> message = messageInfo.message().textWithNoContextMessage();
        if(message.isEmpty()) return;

        String textMessage = message.get();

        handleCatalog(whatsappApi, textMessage, senderJid);

    }

    private boolean shouldReply(Whatsapp whatsappApi, MessageInfo messageInfo, Jid senderJid) {
        boolean whatsappApiIsChatOwner = whatsappApi.store()
                .findMessageById(senderJid, messageInfo.id())
                .isPresent();

        String senderPhoneNumber = senderJid
                .toPhoneNumber() // 5511999999999@whatsapp.co
                .split("@")[0]; // 5511999999999

        boolean replyIsAllowed = allowedNumbers // numeros definidos no application.yml
                .contains(senderPhoneNumber); // contém o número do remetente

        return replyIsAllowed && whatsappApiIsChatOwner;
    }

    private static void handleCatalog(Whatsapp whatsapp, String textMessage, Jid senderJid) {
        if(textMessage.contains("catalogo")) {
            whatsapp.queryBusinessCatalog().whenComplete((catalog, throwable) -> {
                if(throwable != null){
                    log.error("Failed to query business catalog", throwable);
                    return;
                }

                whatsapp.sendMessage(senderJid, "Opa");
                catalog.forEach(catalogItem -> {
                    log.info("Catalog item: {}", catalogItem);
                    whatsapp.sendMessage(senderJid, catalogItem.toString());
                });
            });

        }
    }

    private static boolean listenerIsNotWhoReceivedTheMessage(Whatsapp whatsapp, MessageInfo info) {
        ChatMessageInfo senderChatMessageInfo = (ChatMessageInfo) info;

        Optional<Chat> chatByJid = whatsapp.store().findChatByJid(info.senderJid());
        if(chatByJid.isEmpty()) {
            return true;
        }

        Chat chat = chatByJid.get();
        Jid chatJid = chat.jid();

        whatsapp.store().findMessageById(chatJid, senderChatMessageInfo.id());
        log.info("chat jid: " + chatJid);

        ChatMessageInfo chatMessageInfo = chat.newestMessage().get();


        if(chatMessageInfo != senderChatMessageInfo) {
            log.info("descartando por chat message info");
            return true;
        }
        return false;
    }

    private boolean numberIsAllowedToBeReplied(Jid senderJid) {
        String senderPhoneNumber = senderJid.toPhoneNumber().split("@")[0];
        return allowedNumbers.contains(senderPhoneNumber);
    }
}
