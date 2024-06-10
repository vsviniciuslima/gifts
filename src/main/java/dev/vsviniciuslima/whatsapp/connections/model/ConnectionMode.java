package dev.vsviniciuslima.whatsapp.connections.model;

import it.auties.whatsapp.api.PairingCodeHandler;
import it.auties.whatsapp.api.QrHandler;
import it.auties.whatsapp.api.WebVerificationHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectionMode {
    QR_CODE,
    TOKEN,
    MOBILE;

}
