package dev.vsviniciuslima.whatsapp.connections.service;

import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.button.base.Button;
import it.auties.whatsapp.model.button.base.ButtonText;
import it.auties.whatsapp.model.button.misc.ButtonRow;
import it.auties.whatsapp.model.button.misc.ButtonSection;
import it.auties.whatsapp.model.info.ChatMessageInfo;
import it.auties.whatsapp.model.jid.Jid;
import it.auties.whatsapp.model.message.button.ButtonsMessageHeaderText;
import it.auties.whatsapp.model.message.button.ButtonsMessageSimpleBuilder;
import it.auties.whatsapp.model.message.button.ListMessage;
import it.auties.whatsapp.model.message.button.ListMessageBuilder;
import it.auties.whatsapp.model.message.model.MessageContainerBuilder;
import it.auties.whatsapp.model.message.standard.TextMessage;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@ApplicationScoped
public class ButtonHandler {

    public void sendSimpleOptions(Whatsapp whatsapp, Jid senderJid) {
        var imageButtons = new ButtonsMessageSimpleBuilder()
                .header(new ButtonsMessageHeaderText("Header"))
                .body("A nice body")
                .footer("A nice footer")
                .buttons(createButtons())
                .build();

        whatsapp.sendMessage(senderJid, imageButtons);
        log.info("botões atualizados enviados v4");
    }
    public void sendButtons(Whatsapp whatsapp, Jid contactJid) {
        log.info("Devolvendo send-buttons");
        var buttons = List.of(ButtonRow.of("First option", "A nice description"), ButtonRow.of("Second option", "A nice description"), ButtonRow.of("Third option", "A nice description"));
        var section = new ButtonSection("First section", buttons);
        var otherButtons = List.of(ButtonRow.of("First option", "A nice description"), ButtonRow.of("Second option", "A nice description"), ButtonRow.of("Third option", "A nice description"));
        var anotherSection = new ButtonSection("First section", otherButtons);
        var listMessage = new ListMessageBuilder()
                .sections(List.of(section, anotherSection))
                .button("Click me")
                .title("A nice title")
                .description("A nice description")
                .footer("A nice footer")
                .listType(ListMessage.Type.SINGLE_SELECT)
                .build();

        var container = new MessageContainerBuilder()
                .listMessage(listMessage)
                .textMessage(TextMessage.of("Test"))
                .build();
        whatsapp.sendChatMessage(contactJid, container);
//        var result = whatsapp.sendMessage(messageInfo).join();
        log.info("Terminoou");
    }

    public List<Button> createButtons()  {
        return IntStream.range(0, 3)
                .mapToObj(index -> new ButtonText("Button %s".formatted(index)))
                .map(Button::of)
                .toList();
    }
}
