package com.example.rent_yourcomfort.dto;

import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
    private String country;
    private String region;
    private String city;
    private String street;
    private int houseNumber;
    private Integer apartmentNumber;
    private String zipCode;
    private Housing housing;

    public LocationDTO(String country, String region, String city, String street, int houseNumber, Integer apartmentNumber, String zipCode) {
    }

    public static LocationDTO convertToDTO(Location location) {
        return new LocationDTO(
                location.getCountry(),
                location.getRegion(),
                location.getCity(),
                location.getStreet(),
                location.getHouseNumber(),
                location.getApartmentNumber(),
                location.getZipCode()
        );
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
