package org.example.flightticketbooking.service;

import org.example.flightticketbooking.domain.Booking;
import org.example.flightticketbooking.domain.FlightInventory;
import org.example.flightticketbooking.dto.BookingResponse;
import org.example.flightticketbooking.dto.CreateBookingRequest;
import org.example.flightticketbooking.exception.BookingNotFoundException;
import org.example.flightticketbooking.exception.FlightNotFoundException;
import org.example.flightticketbooking.exception.NoSeatsAvailableException;
import org.example.flightticketbooking.repository.BookingRepository;
import org.example.flightticketbooking.repository.FlightInventoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightInventoryRepository flightInventoryRepository;

    public BookingService(BookingRepository bookingRepository, FlightInventoryRepository flightInventoryRepository) {
        this.bookingRepository = bookingRepository;
        this.flightInventoryRepository = flightInventoryRepository;
    }

    public BookingResponse createBooking(CreateBookingRequest request) {
        FlightInventory flightInventory = flightInventoryRepository.findByFlightNumber(request.flightNumber())
                .orElseThrow(() -> new FlightNotFoundException(request.flightNumber()));

        boolean reserved = flightInventory.reserveOneSeat();
        if (!reserved) {
            throw new NoSeatsAvailableException(request.flightNumber());
        }

        Booking booking = new Booking(
                UUID.randomUUID(),
                request.flightNumber(),
                request.passengerName(),
                Instant.now()
        );

        bookingRepository.save(booking);

        return new BookingResponse(
                booking.id(),
                booking.flightNumber(),
                booking.passengerName(),
                booking.createdAt()
        );
    }

    public void cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.deleteById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        flightInventoryRepository.findByFlightNumber(booking.flightNumber())
                .ifPresent(FlightInventory::releaseOneSeat);
    }
}

