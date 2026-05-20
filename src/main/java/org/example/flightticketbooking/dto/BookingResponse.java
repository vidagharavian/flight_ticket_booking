package org.example.flightticketbooking.dto;

import java.time.Instant;
import java.util.UUID;

public record BookingResponse(
        UUID bookingId,
        String flightNumber,
        String passengerName,
        Instant createdAt
) {
}

