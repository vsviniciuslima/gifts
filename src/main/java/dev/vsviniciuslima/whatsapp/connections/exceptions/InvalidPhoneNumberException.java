package dev.vsviniciuslima.whatsapp.connections.exceptions;

public class InvalidPhoneNumberException extends ConnectionException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String message, long userPhoneNumber) {
        super(message, userPhoneNumber);
    }
}
