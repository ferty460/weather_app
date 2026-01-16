package org.example.weatherapp.exception.location;

public class LocationAlreadyExistsException extends RuntimeException {

    public LocationAlreadyExistsException() {
        super("Location already exists in your list");
    }

}
