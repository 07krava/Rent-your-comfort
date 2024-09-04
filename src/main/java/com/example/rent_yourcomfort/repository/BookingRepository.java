package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.Booking;
import com.example.rent_yourcomfort.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByHousingAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Housing housing, Date startDate, Date endDate);

}
