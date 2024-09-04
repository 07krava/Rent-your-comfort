package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.exception.HousingAlreadyBookedException;
import com.example.rent_yourcomfort.exception.InsufficientFundsException;
import com.example.rent_yourcomfort.exception.MaximumOccupantsExceededException;
import com.example.rent_yourcomfort.exception.PastBookingException;
import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/housing/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody Booking booking) {
        try {
            bookingService.createBooking(booking);
            return ResponseEntity.ok("The housing is booked successfully");
        } catch (PastBookingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking is not possible in the past. Booking is only possible from today's date and future dates.");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not enough money in your account. Please top up your account.");
        } catch (MaximumOccupantsExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Maximum number of occupants exceeded.");
        } catch (HousingAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The housing is already booked for the dates indicated.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unknown error occurred.");
        }
    }

    @GetMapping("/allBookingsById/{id}")
    public ResponseEntity<?> getBookingsByUserId(@PathVariable Long id) {
        try {
            bookingService.getBookingsByUserId(id);
            return ResponseEntity.ok("");
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("User with id " + id + " not found.");
        }
    }
}

