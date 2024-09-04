package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.model.Housing;
import java.util.Date;
import java.util.List;

public interface BookingService {

    boolean isHousingAvailableByDates(Housing housing, Date startDate, Date endDate);

    Booking createBooking(Booking booking);

    List<Booking> getBookingsByUserId(Long userId);

    List<Booking> getBookingsForOwner(Long ownerId);

    void approveBooking(Long bookingId);

    void rejectBooking(Long bookingId);
}
