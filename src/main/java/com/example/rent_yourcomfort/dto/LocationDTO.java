package com.example.rent_yourcomfort.dto;

import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Location;
import lombok.Data;

@Data
public class LocationDTO {
    private String country;
    private String region;
    private String city;
    private String street;
    private int houseNumber;
    private Integer apartmentNumber;
    private String zipCode;
    private Housing housing;

    public static LocationDTO convertToLocationDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCountry(location.getCountry());
        locationDTO.setRegion(location.getRegion());
        locationDTO.setCity(location.getCity());
        locationDTO.setStreet(location.getStreet());
        locationDTO.setHouseNumber(location.getHouseNumber());
        locationDTO.setApartmentNumber(location.getApartmentNumber());
        locationDTO.setZipCode(location.getZipCode());
        locationDTO.setHousing(location.getHousing());
        return locationDTO;
    }

    public static Location convertToLocation(LocationDTO locationDTO){
        Location location = new Location();
        location.setCountry(locationDTO.getCountry());
        location.setRegion(locationDTO.getRegion());
        location.setCity(locationDTO.getCity());
        location.setStreet(locationDTO.getStreet());
        location.setHouseNumber(locationDTO.getHouseNumber());
        location.setApartmentNumber(locationDTO.getApartmentNumber());
        location.setZipCode(locationDTO.getZipCode());
        location.setHousing(locationDTO.getHousing());
        return location;
    }
}
