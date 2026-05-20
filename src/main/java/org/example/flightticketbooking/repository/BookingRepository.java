package org.example.flightticketbooking.repository;

import org.example.flightticketbooking.domain.Booking;

import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(UUID id);

    Optional<Booking> deleteById(UUID id);
}

