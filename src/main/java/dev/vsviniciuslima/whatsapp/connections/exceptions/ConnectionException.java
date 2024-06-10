package dev.vsviniciuslima.whatsapp.connections.exceptions;

import lombok.Getter;

@Getter
public class ConnectionException extends RuntimeException {

    private long userPhoneNumber;
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, long userPhoneNumber) {
        super(message);
        this.userPhoneNumber = userPhoneNumber;
    }
}
