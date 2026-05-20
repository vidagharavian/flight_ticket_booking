package org.example.flightticketbooking.exception;

public class NoSeatsAvailableException extends RuntimeException {
    public NoSeatsAvailableException(String flightNumber) {
        super("No seats available for flight: " + flightNumber);
    }
}

