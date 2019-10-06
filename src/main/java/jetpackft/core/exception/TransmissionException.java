package jetpackft.core.exception;

import jetpackft.core.transmission.Transmission;

import java.io.IOException;

public class TransmissionException extends IOException {

    private String message;
    private Transmission transmission;

    public TransmissionException(Transmission _transmission, String _message) {
        message = _message;
        transmission = _transmission;
    }

    public TransmissionException(String _message) {
        message = _message;
    }

    public Transmission getTransmission() {
        if (transmission == null) {
            throw new NullPointerException("The exception does not have a transmission attached to it.");
        }
        return transmission;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
