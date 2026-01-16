package org.example.weatherapp.exception.auth;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String name) {
        super("User with login " + name + " not found");
    }

}
