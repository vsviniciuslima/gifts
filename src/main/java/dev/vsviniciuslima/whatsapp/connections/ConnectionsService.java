package dev.vsviniciuslima.whatsapp.connections;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.vsviniciuslima.whatsapp.connections.exceptions.ConnectionException;
import dev.vsviniciuslima.whatsapp.connections.model.*;
import dev.vsviniciuslima.whatsapp.connections.service.ConnectionCreator;
import dev.vsviniciuslima.whatsapp.connections.service.ListenerManager;
import dev.vsviniciuslima.whatsapp.connections.service.QrCodeFileHandler;
import it.auties.whatsapp.api.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
@ApplicationScoped
public class ConnectionsService {


    @Inject
    ConnectionCreator connectionCreator;

    @Inject
    QrCodeFileHandler qrCodeFileHandler;

    @Inject
    ListenerManager listenerManager;

    public Whatsapp createConnection(CreateConnection newConnection) {

        Whatsapp whatsapp = switch (newConnection.connectionMode()) {
            case MOBILE:
                yield connectionCreator.createMobileConnection(newConnection);
            case TOKEN:
                yield connectionCreator.createTokenConnection(newConnection);
            case QR_CODE:
                yield connectionCreator.createQrCodeConnection(newConnection);
        };


        CreateConnectionResponse response = new CreateConnectionResponse();
        CompletableFuture<Whatsapp> future = listenerManager
                .addListeners(whatsapp)
                .connect()
                .thenApply(connectedApi -> {
                    if (!connectedApi.isConnected()) {
                        System.out.println("Reconnecting: " + System.currentTimeMillis() + ")");
//                        connectedApi.reconnect().join();
//                        System.out.println("Reconnected: " + System.currentTimeMillis() + ")");
                    } else if (connectedApi.store().jid().isPresent()) {
                        long now = System.currentTimeMillis();
                        System.out.println("Trying to send a message: " + now);
                    }
                    return connectedApi;
                });

        return future.join();

    }

    public byte[] createQrCodeConnection(CreateConnection newConnection) {
        Whatsapp whatsapp = connectionCreator.createQrCodeConnection(newConnection);

        return listenerManager
                .addListeners(whatsapp)
                .connect()
                .thenApply(connectedApi -> qrCodeFileHandler.getQrCode())
                .join();
    }


    public Response listConnections() {
        return Response.ok().entity(Connection.listAll()).build();
    }

    public void disconnect(DeleteConnection connection) {
        Optional<Whatsapp> whatsappConnection = Whatsapp.webBuilder()
                .newConnection(connection.phoneNumber())
                .registered();

        if(whatsappConnection.isEmpty()) throw new ConnectionException("A conexão não está registrada", connection.phoneNumber());

        Whatsapp whatsapp = whatsappConnection.get();

        if(!whatsapp.isConnected()) throw new ConnectionException("A conexão não está ativa", connection.phoneNumber());

        whatsapp.logout().whenComplete((aVoid, throwable) -> {
            if(throwable != null) {
                log.error("Error logging out", throwable);
            }
            log.info("Logged out succesfully");
        });

    }
}
