package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.configuration.SecurityUtil;
import com.example.rent_yourcomfort.exception.*;
import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.User;
import com.example.rent_yourcomfort.repository.BookingRepository;
import com.example.rent_yourcomfort.repository.HousingRepository;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final HousingRepository housingRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(HousingRepository housingRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public boolean isHousingAvailableByDates(Housing housing, Date startDate, Date endDate) {
        List<Booking> bookings = bookingRepository.findByHousingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(housing, startDate, endDate);

        for (Booking booking : bookings) {
            if ("rejected".equals(booking.getStatus())) {
                return true;  // Если найдено отклоненное бронирование, разрешаем бронирование на указанные даты
            }
        }
        return bookings.isEmpty();
    }

    public Booking createBooking(Booking booking) {
        User renter = userRepository.findById(booking.getRenter().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + booking.getRenter().getId()));
        Housing housing = housingRepository.findById(booking.getHousing().getId())
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + booking.getHousing().getId()));

        // Поле ниже нужно для добавления этой переменной в базу данных в таблицу booking
        BigDecimal totalAmount = housing.getPrice();
        booking.setTotalAmountOfMoney(totalAmount);
        BigDecimal renterBalance = renter.getWallet().getBalance();

        // Проверка на дату начала бронирования
        if (booking.getStartDate().before(new Date())) {
            throw new PastBookingException("Booking is not possible in the past. Booking is only possible from today's date and future dates.");
        }

        if (housing.getOwner().equals(renter)) {
            processOwnerBooking(booking, housing, renter);
        } else {
            processRenterBooking(booking, housing, renter, totalAmount, renterBalance);
        }

        return booking;
    }

    private void processOwnerBooking(Booking booking, Housing housing, User renter) {
        if (housing.getMaxAmountPeople() >= booking.getGuests()) {
            setCommonBookingFields(booking, housing, renter);
            booking.setStatus("approved");
            validateAndCreateBooking(booking, housing);
        } else {
            throw new MaximumOccupantsExceededException("Maximum number of occupants exceeded.");
        }
    }

    private void processRenterBooking(Booking booking, Housing housing, User renter, BigDecimal totalAmount, BigDecimal renterBalance) {
        if (renterBalance.compareTo(totalAmount) >= 0) {
            renter.getWallet().setBalance(renterBalance.subtract(totalAmount));
            renter.getWallet().setFrozenBalance(totalAmount);
            if (housing.getMaxAmountPeople() >= booking.getGuests()) {
                setCommonBookingFields(booking, housing, renter);
                booking.setStatus("pending");
                validateAndCreateBooking(booking, housing);
            } else {
                throw new MaximumOccupantsExceededException("Maximum number of occupants exceeded.");
            }
        } else {
            throw new InsufficientFundsException("Not enough money in your account. Please top up your account.");
        }
    }

    private void setCommonBookingFields(Booking booking, Housing housing, User renter) {
        booking.setOwner(housing.getOwner());
        booking.setHousing(housing);
    }

    private void validateAndCreateBooking(Booking booking, Housing housing) {
        if (!isHousingAvailableByDates(housing, booking.getStartDate(), booking.getEndDate())) {
            throw new HousingAlreadyBookedException("The housing is already booked for the dates indicated.");
        }
        createNewBooking(booking);
    }

    private void createNewBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    //toDo Нужно протестировать этот метод getBookingsByUserId

    public List<Booking> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        List<Booking> listBooking = user.getBookings();
        return listBooking;
    }

    @Override
    public List<Booking> getBookingsForOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));

        List<Booking> bookings = owner.getBookings();
        if (bookings == null) {
            throw new RuntimeException("The owner hasn't any bookings.");
        }

        return bookingRepository.findAll();
    }

    public void approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!"pending".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot approve a booking that is not in pending status.");
        }

        BigDecimal totalAmount = booking.getHousing().getPrice();
        User owner = booking.getHousing().getOwner();
        User renter = booking.getRenter();

        // Проверка, что пользователь, пытающийся подтвердить бронирование, является владельцем жилья
        String currentUsername = SecurityUtil.getCurrentUsername();
        if (!owner.getEmail().equals(currentUsername)) {
            throw new UnauthorizedException("You are not authorized to approve this booking.");
        }

        // Перевод средств от renter к owner
        owner.getWallet().setBalance(totalAmount);
        renter.getWallet().setFrozenBalance(BigDecimal.ZERO);

        booking.setStatus("approved");
        bookingRepository.save(booking);
    }

    public void rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));

        if (!"pending".equals(booking.getStatus())) {
            throw new RuntimeException("Cannot reject a booking that is not in pending status.");
        }

        BigDecimal totalAmount = booking.getHousing().getPrice();
        User renter = booking.getRenter();
        User owner = booking.getOwner();

        // Возврат средств на счет renter
        renter.getWallet().setBalance(renter.getWallet().getBalance().add(totalAmount));
        renter.getWallet().setFrozenBalance(BigDecimal.ZERO);

        booking.setStatus("rejected");
        bookingRepository.save(booking);
    }
}
