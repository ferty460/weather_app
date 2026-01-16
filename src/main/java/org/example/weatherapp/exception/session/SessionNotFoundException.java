package org.example.weatherapp.exception.session;

public class SessionNotFoundException extends RuntimeException {

    public SessionNotFoundException() {
        super("Session not found");
    }

}
