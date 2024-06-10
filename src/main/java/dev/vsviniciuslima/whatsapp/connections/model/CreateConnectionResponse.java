package dev.vsviniciuslima.whatsapp.connections.model;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CreateConnectionResponse {
    private byte[] qrCode;
    private String qrCodeString;
}
