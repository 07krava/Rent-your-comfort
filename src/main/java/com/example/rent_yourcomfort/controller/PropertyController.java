package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.dto.HousingDTO;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Property;
import com.example.rent_yourcomfort.service.PropertyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public List<HousingDTO> getFilteredProperties(@RequestParam(required = false) BigDecimal minPrice,
                                                  @RequestParam(required = false) BigDecimal maxPrice,
                                                  @RequestParam(required = false) String housingType,
                                                  @RequestParam(required = false) Integer bedRooms,
                                                  @RequestParam(required = false) Integer bathRooms,
                                                  @RequestParam(required = false) Integer maxAmountPeople,
                                                  @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                  @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                                  @RequestParam(required = false) Integer beds,
                                                  @RequestParam(required = false) String city
    ) {

        List<Property> filteredProperties = propertyService.getFilteredProperties(
                minPrice,
                maxPrice,
                housingType,
                bedRooms,
                bathRooms,
                maxAmountPeople,
                startDate,
                endDate,
                beds,
                city);

        List<Housing> properties = new ArrayList<>();

        for(Housing housing : filteredProperties){
            Housing home = new Housing();
            home.setId(housing.getId());
            home.setTitle(housing.getTitle());
            home.setDescription(housing.getDescription());
            home.setMaxAmountPeople(housing.getMaxAmountPeople());
            home.setPrice(housing.getPrice());
            home.setBedRooms(housing.getBedRooms());
            home.setBeds(housing.getBeds());
            home.setBathRooms(housing.getBathRooms());
            home.setHousingType(housing.getHousingType());
            home.setLocation(housing.getLocation());
            home.setPhotos(housing.getPhotos());
            home.setOwner(housing.getOwner());

            properties.add(home);

        }

        return properties.stream()
                .map(HousingDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}

