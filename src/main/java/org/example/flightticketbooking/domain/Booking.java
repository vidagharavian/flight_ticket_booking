package org.example.flightticketbooking.domain;

import java.time.Instant;
import java.util.UUID;

public record Booking(
        UUID id,
        String flightNumber,
        String passengerName,
        Instant createdAt
) {
}

