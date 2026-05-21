# Flight Ticket Booking API

## Current Step: Architecture + Service + API Layer

This iteration includes the full booking flow from HTTP API down to in-memory business logic.

### Package layout

- `org.example.flightticketbooking.domain` - core business models (`Booking`, `FlightInventory`)
- `org.example.flightticketbooking.dto` - request/response contracts (`CreateBookingRequest`, `BookingResponse`, `ApiError`)
- `org.example.flightticketbooking.repository` - in-memory persistence interfaces and implementations
- `org.example.flightticketbooking.service` - booking use cases (`createBooking`, `cancelBooking`)
- `org.example.flightticketbooking.exception` - domain/application exceptions
- `org.example.flightticketbooking.controller` - REST API controller + exception handler

### Implemented endpoints

- `POST /api/bookings` - create booking, returns `201 Created`
- `DELETE /api/bookings/{bookingId}` - cancel booking, returns `204 No Content`

### Error mapping

- unknown flight -> `404 Not Found`
- no seats available -> `409 Conflict`
- unknown booking -> `404 Not Found`
- validation error -> `400 Bad Request`

### Example requests

```zsh
curl -i -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{"flightNumber":"FL-100","passengerName":"Alice"}'

curl -i -X DELETE http://localhost:8080/api/bookings/<bookingId>
```

### Test coverage

- service tests for booking rules and cancellation
- integration tests for API status codes (`201`, `204`, `409`)

### Next steps

- Add a Maven wrapper (`mvnw`) for consistent local execution
- Expand integration tests for `400` and `404` cases
- Prepare final submission README section: "what to improve with more time"
