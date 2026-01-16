package org.example.weatherapp.exception.auth;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String login) {
        super("User with username '" + login + "' already exists");
    }

}
