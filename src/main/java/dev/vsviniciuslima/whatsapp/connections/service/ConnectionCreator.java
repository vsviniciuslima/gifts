package dev.vsviniciuslima.whatsapp.connections.service;

import dev.vsviniciuslima.whatsapp.connections.exceptions.ConnectionErrorHandler;
import dev.vsviniciuslima.whatsapp.connections.listeners.DisconnectListener;
import dev.vsviniciuslima.whatsapp.connections.listeners.LoggedInListener;
import dev.vsviniciuslima.whatsapp.connections.listeners.MessageListener;
import dev.vsviniciuslima.whatsapp.connections.model.CreateConnection;
import it.auties.whatsapp.api.PairingCodeHandler;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebOptionsBuilder;
import it.auties.whatsapp.api.Whatsapp;
import it.auties.whatsapp.model.companion.CompanionDevice;
import it.auties.whatsapp.model.mobile.VerificationCodeMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.function.Consumer;

@ApplicationScoped
@Slf4j
public class ConnectionCreator {

    @Inject
    ConnectionErrorHandler errorHandler;

    @Inject
    CodeSupplier codeSupplier;

    @Inject
    ListenerManager listenerManager;

    public WebOptionsBuilder getWebOptionsBuilder() {
        return Whatsapp.webBuilder()
                .newConnection()
                .name("Outcome Cloud")
                .errorHandler(errorHandler);
    }
    public Whatsapp createQrCodeConnection(CreateConnection newConnection) {

        log.info("Creating QR Code connection");

        Path path = Path.of("qr.png");
        QrHandler.ToFileConsumer discarding = QrHandler.ToFileConsumer.discarding();
        QrHandler qrHandler = QrHandler.toFile(path, discarding);
        return this.getWebOptionsBuilder().unregistered(qrHandler);
    }

    public Whatsapp createTokenConnection(CreateConnection newConnection) {
        Long phoneNumber = newConnection
                .phoneNumber()
                .orElseThrow();
        PairingCodeHandler pairingCodeHandler = PairingCodeHandler.toTerminal();
        Consumer<String> codeHandler = pairingCodeHandler.andThen((log::info));

        return this.getWebOptionsBuilder()
                .unregistered(phoneNumber, pairingCodeHandler);
    }

    public Whatsapp createMobileConnection(CreateConnection newConnection) {

        Long phoneNumber = newConnection
                .phoneNumber()
                .orElseThrow(() -> new BadRequestException("Phone number is required on Mobile connection mode"));

        Whatsapp mobileConnection = Whatsapp.mobileBuilder()
                .newConnection(phoneNumber)
                .name("Outcome Cloud")
                .errorHandler(errorHandler)
                .device(CompanionDevice.ios(false))
                .unregistered()

                .verificationCodeMethod(VerificationCodeMethod.WHATSAPP)
                .verificationCodeSupplier(() -> codeSupplier.setPhoneNumber(phoneNumber).get().join())
                .register(phoneNumber)
                .join()
                .whatsapp();

        return listenerManager.addListeners(mobileConnection);
    }
}
