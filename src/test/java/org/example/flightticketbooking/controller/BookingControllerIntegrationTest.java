package org.example.flightticketbooking.controller;

import org.example.flightticketbooking.dto.CreateBookingRequest;
import org.example.flightticketbooking.dto.BookingResponse;
import org.example.flightticketbooking.dto.ApiError;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String bookingsUrl() {
        return "http://localhost:" + port + "/api/bookings";
    }

    @Test
    void createBookingShouldReturn201() {
        ResponseEntity<BookingResponse> response = restTemplate.postForEntity(
                bookingsUrl(),
                new CreateBookingRequest("FL-100", "Alice"),
                BookingResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(Objects.requireNonNull(response.getBody()).bookingId());
    }

    @Test
    void createBookingShouldReturn409WhenFlightIsFull() {
        String url = bookingsUrl();

        ResponseEntity<BookingResponse> first = restTemplate.postForEntity(
                url,
                new CreateBookingRequest("FL-300", "Alice"),
                BookingResponse.class
        );

        ResponseEntity<ApiError> second = restTemplate.postForEntity(
                url,
                new CreateBookingRequest("FL-300", "Bob"),
                ApiError.class
        );

        assertEquals(HttpStatus.CREATED, first.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, second.getStatusCode());
    }

    @Test
    void cancelBookingShouldReturn204() {
        String baseUrl = bookingsUrl();

        ResponseEntity<BookingResponse> created = restTemplate.postForEntity(
                baseUrl,
                new CreateBookingRequest("FL-200", "Charlie"),
                BookingResponse.class
        );

        String bookingId = Objects.requireNonNull(created.getBody()).bookingId().toString();

        ResponseEntity<Void> cancelResponse = restTemplate.exchange(
                baseUrl + "/" + bookingId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, cancelResponse.getStatusCode());
    }

    @Test
    void cancelBookingShouldReturn404WhenBookingDoesNotExist() {
        UUID unknownBookingId = UUID.randomUUID();

        ResponseEntity<ApiError> response = restTemplate.exchange(
                bookingsUrl() + "/" + unknownBookingId,
                HttpMethod.DELETE,
                null,
                ApiError.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createBookingShouldReturn404WhenFlightDoesNotExist() {
        ResponseEntity<ApiError> response = restTemplate.postForEntity(
                bookingsUrl(),
                new CreateBookingRequest("FL-999", "Alice"),
                ApiError.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Not Found", response.getBody().error());
        assertEquals("/api/bookings", response.getBody().path());
    }

    @Test
    void createBookingShouldReturn400WhenFlightNumberIsBlank() {
        ResponseEntity<ApiError> response = restTemplate.postForEntity(
                bookingsUrl(),
                new CreateBookingRequest("", "Alice"),
                ApiError.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Bad Request", response.getBody().error());
        assertEquals("/api/bookings", response.getBody().path());
    }

    @Test
    void createBookingShouldReturn400WhenPassengerNameIsBlank() {
        ResponseEntity<ApiError> response = restTemplate.postForEntity(
                bookingsUrl(),
                new CreateBookingRequest("FL-100", ""),
                ApiError.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Bad Request", response.getBody().error());
    }
}





