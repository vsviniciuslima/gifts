package dev.vsviniciuslima.whatsapp.connections.service;

import it.auties.whatsapp.api.Whatsapp;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@ApplicationScoped
public class QrCodeFileHandler {

    public byte[] getQrCode() {
        Path path = Paths.get("qr.png");
        File qrCodeFile = path.toFile();

        byte[] imageBytes;

        try {
            while (!qrCodeFile.exists()) {
                log.info("Ainda nao existe... tentando dnv");
                Thread.sleep(300);
            }
            imageBytes = Files.readAllBytes(path);
        } catch (Exception e) {
            log.info("fodeu kkkk");
            throw new RuntimeException(e);
        }

        qrCodeFile.delete();

        return imageBytes;
    }
}
