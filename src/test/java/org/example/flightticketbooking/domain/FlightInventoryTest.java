package org.example.flightticketbooking.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightInventoryTest {

    @Test
    void constructorShouldRejectNegativeCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new FlightInventory("FL-100", -1));
    }

    @Test
    void reserveShouldFailWhenNoSeatsLeft() {
        FlightInventory inventory = new FlightInventory("FL-300", 1);

        assertTrue(inventory.reserveOneSeat());
        assertFalse(inventory.reserveOneSeat());
        assertEquals(0, inventory.getRemainingSeats());
    }

    @Test
    void releaseShouldNotIncreaseBeyondCapacity() {
        FlightInventory inventory = new FlightInventory("FL-100", 2);

        inventory.releaseOneSeat();
        assertEquals(2, inventory.getRemainingSeats());

        assertTrue(inventory.reserveOneSeat());
        assertEquals(1, inventory.getRemainingSeats());

        inventory.releaseOneSeat();
        inventory.releaseOneSeat();
        assertEquals(2, inventory.getRemainingSeats());
    }

    @Test
    void gettersShouldReturnConfiguredValues() {
        FlightInventory inventory = new FlightInventory("FL-200", 3);

        assertEquals("FL-200", inventory.getFlightNumber());
        assertEquals(3, inventory.getCapacity());
        assertEquals(3, inventory.getRemainingSeats());
    }
}

