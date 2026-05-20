package org.example.flightticketbooking.repository;

import org.example.flightticketbooking.domain.Booking;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBookingRepository implements BookingRepository {
    private final Map<UUID, Booking> storage = new ConcurrentHashMap<>();

    @Override
    public Booking save(Booking booking) {
        storage.put(booking.id(), booking);
        return booking;
    }

    @Override
    public Optional<Booking> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<Booking> deleteById(UUID id) {
        return Optional.ofNullable(storage.remove(id));
    }
}

