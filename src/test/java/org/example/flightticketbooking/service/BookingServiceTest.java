package org.example.flightticketbooking.service;

import org.example.flightticketbooking.dto.BookingResponse;
import org.example.flightticketbooking.dto.CreateBookingRequest;
import org.example.flightticketbooking.exception.BookingNotFoundException;
import org.example.flightticketbooking.exception.FlightNotFoundException;
import org.example.flightticketbooking.exception.NoSeatsAvailableException;
import org.example.flightticketbooking.repository.InMemoryBookingRepository;
import org.example.flightticketbooking.repository.InMemoryFlightInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(
                new InMemoryBookingRepository(),
                new InMemoryFlightInventoryRepository()
        );
    }

    @Test
    void createBookingShouldReturnBookingResponse() {
        BookingResponse response = bookingService.createBooking(
                new CreateBookingRequest("FL-100", "Alice")
        );

        assertNotNull(response.bookingId());
        assertEquals("FL-100", response.flightNumber());
        assertEquals("Alice", response.passengerName());
        assertNotNull(response.createdAt());
    }

    @Test
    void createBookingShouldFailWhenFlightDoesNotExist() {
        assertThrows(FlightNotFoundException.class, () ->
                bookingService.createBooking(new CreateBookingRequest("FL-999", "Alice"))
        );
    }

    @Test
    void createBookingShouldPreventOverbooking() {
        bookingService.createBooking(new CreateBookingRequest("FL-300", "Alice"));

        assertThrows(NoSeatsAvailableException.class, () ->
                bookingService.createBooking(new CreateBookingRequest("FL-300", "Bob"))
        );
    }

    @Test
    void cancelBookingShouldReleaseSeat() {
        BookingResponse booking = bookingService.createBooking(
                new CreateBookingRequest("FL-300", "Alice")
        );

        bookingService.cancelBooking(booking.bookingId());

        assertDoesNotThrow(() ->
                bookingService.createBooking(new CreateBookingRequest("FL-300", "Bob"))
        );
    }

    @Test
    void cancelBookingShouldFailWhenBookingDoesNotExist() {
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.cancelBooking(UUID.randomUUID())
        );
    }
}

