package com.example.rent_yourcomfort.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Date startDate;
    private Date endDate;
    private String status;

}
