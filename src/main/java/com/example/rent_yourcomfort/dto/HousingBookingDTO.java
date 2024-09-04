package com.example.rent_yourcomfort.dto;

import com.example.rent_yourcomfort.model.HousingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HousingBookingDTO {
    private Long id;
    private BigDecimal price;
    private int bedRooms;
    private int beds;
    private int bathRooms;
    private String description;
    private String title;
    private int maxAmountPeople;
    private HousingType housingType;
    private boolean active;
    private List<BookingDTO> bookings;

}
