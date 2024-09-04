package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.model.Property;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface PropertyService {

    List<Property> getFilteredProperties(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String housingType,
            Integer bedCount,
            Integer bathCount,
            Integer maxAmountPeople,
            Date startDate,
            Date endDate,
            Integer beds,
            String city);
}
