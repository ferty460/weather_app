package org.example.weatherapp.exception.location;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException() {
        super("Location not found");
    }

}
