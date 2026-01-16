package org.example.weatherapp.exception.auth;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid login or password");
    }

}
