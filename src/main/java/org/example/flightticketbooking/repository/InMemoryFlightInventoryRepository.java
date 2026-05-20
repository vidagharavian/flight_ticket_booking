package org.example.flightticketbooking.repository;

import org.example.flightticketbooking.domain.FlightInventory;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryFlightInventoryRepository implements FlightInventoryRepository {
    private final Map<String, FlightInventory> storage = new ConcurrentHashMap<>();

    public InMemoryFlightInventoryRepository() {
        // Seed a few flights to keep the API testable without a DB.
        storage.put("FL-100", new FlightInventory("FL-100", 2));
        storage.put("FL-200", new FlightInventory("FL-200", 3));
        storage.put("FL-300", new FlightInventory("FL-300", 1));
    }

    @Override
    public Optional<FlightInventory> findByFlightNumber(String flightNumber) {
        return Optional.ofNullable(storage.get(flightNumber));
    }
}

