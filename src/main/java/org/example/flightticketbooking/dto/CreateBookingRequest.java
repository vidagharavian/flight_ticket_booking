package org.example.flightticketbooking.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBookingRequest(
        @NotBlank(message = "flightNumber is required")
        String flightNumber,
        @NotBlank(message = "passengerName is required")
        String passengerName
) {
}

