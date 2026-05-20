package org.example.flightticketbooking.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class FlightInventory {
    private final String flightNumber;
    private final int capacity;
    private final AtomicInteger remainingSeats;

    public FlightInventory(String flightNumber, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be non-negative");
        }
        this.flightNumber = flightNumber;
        this.capacity = capacity;
        this.remainingSeats = new AtomicInteger(capacity);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRemainingSeats() {
        return remainingSeats.get();
    }

    public boolean reserveOneSeat() {
        while (true) {
            int current = remainingSeats.get();
            if (current <= 0) {
                return false;
            }
            if (remainingSeats.compareAndSet(current, current - 1)) {
                return true;
            }
        }
    }

    public void releaseOneSeat() {
        remainingSeats.updateAndGet(current -> Math.min(current + 1, capacity));
    }
}

