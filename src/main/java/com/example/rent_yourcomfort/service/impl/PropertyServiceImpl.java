package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.service.HistoryService;
import com.example.rent_yourcomfort.service.HousingService;
import com.example.rent_yourcomfort.service.PropertyService;
import com.example.rent_yourcomfort.model.History;
import com.example.rent_yourcomfort.model.Property;
import com.example.rent_yourcomfort.repository.PropertyRepository;
import com.example.rent_yourcomfort.specification.PropertySpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PhotoServiceImpl photoServiceImpl;
    private final HousingService housingService;
    private final HistoryService historyService;

    public PropertyServiceImpl(PropertyRepository propertyRepository, HistoryService historyService,
                               PhotoServiceImpl photoServiceImpl, HousingService housingService
    ) {
        this.propertyRepository = propertyRepository;
        this.photoServiceImpl = photoServiceImpl;
        this.housingService = housingService;
        this.historyService = historyService;
    }

    @Override
    public List<Property> getFilteredProperties(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String housingType,
            Integer bedRooms,
            Integer bathRooms,
            Integer maxAmountPeople,
            Date startDate,
            Date endDate,
            Integer beds,
            String city) {

        History history = new History();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
        } else {
            System.out.println("User is not authenticated.");
        }
        history.setMinPrice(minPrice);
        history.setMaxPrice(maxPrice);
        history.setHousingType(housingType);
        if (bedRooms != null) {
            history.setBedRooms(bedRooms);
        }
        if (bathRooms != null) {
            history.setBathRooms(bathRooms);
        }
        if (maxAmountPeople != null) {
            history.setMaxAmountPeople(maxAmountPeople);
        }
        history.setStartDate(startDate);
        history.setEndDate(endDate);
        if (beds != null) {
            history.setBeds(beds);
        }
        history.setCity(city);
        history.setDateOfRequest(new Date());

        historyService.saveHistory(history);

        Specification<Property> spec = Specification.where(null);

        if (minPrice != null && maxPrice != null) {
            spec = spec.and(PropertySpecifications.filterByPriceRange(minPrice, maxPrice));
        }

        if (housingType != null) {
            spec = spec.and(PropertySpecifications.filterByPropertyType(housingType));
        }

        if (bedRooms != null) {
            spec = spec.and(PropertySpecifications.filterByBedCount(bedRooms));
        }

        if (bathRooms != null) {
            spec = spec.and(PropertySpecifications.filterByBathCount(bathRooms));
        }

        if (maxAmountPeople != null) {
            spec = spec.and(PropertySpecifications.filterByMaxPeoples(maxAmountPeople));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and(PropertySpecifications.filterByAvailability(startDate, endDate));
        }

        if (beds != null) {
            spec = spec.and(PropertySpecifications.filterByQuantityBeds(beds));
        }

        if (city != null) {
            spec = spec.and(PropertySpecifications.filterByCity(city));
        }

        return propertyRepository.findAll(spec);
    }
}


