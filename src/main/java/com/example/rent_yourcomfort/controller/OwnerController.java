package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.exception.UnauthorizedException;
import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.service.BookingService;
import com.example.rent_yourcomfort.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public OwnerController(UserService userService, BookingService bookingService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/bookings/{ownerId}")
    public List<Booking> getBookingsForOwner(@PathVariable Long ownerId) {
        return bookingService.getBookingsForOwner(ownerId);
    }

    @PutMapping("/approve/{bookingId}")
    public ResponseEntity<String> approveBooking(@PathVariable Long bookingId) {
        try {
            bookingService.approveBooking(bookingId);
            return ResponseEntity.ok("Booking approved successfully.");
        }catch ( UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not authorized to approve this booking.");
        }
    }

    @PutMapping("/reject/{bookingId}")
    public ResponseEntity<String> rejectBooking(@PathVariable Long bookingId) {
        bookingService.rejectBooking(bookingId);
        return ResponseEntity.ok("Booking rejected successfully.");
    }
}

