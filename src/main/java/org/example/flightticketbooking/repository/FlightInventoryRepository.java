package org.example.flightticketbooking.repository;

import org.example.flightticketbooking.domain.FlightInventory;

import java.util.Optional;

public interface FlightInventoryRepository {
    Optional<FlightInventory> findByFlightNumber(String flightNumber);
}

