package org.example.weatherapp.exception.session;

public class SessionExpiredException extends RuntimeException {

    public SessionExpiredException() {
        super("Session expired");
    }

}
