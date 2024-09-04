package com.example.rent_yourcomfort.dto;

import com.example.rent_yourcomfort.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class HousingDTO {
    private Long id;
    private BigDecimal price;
    private String description;
    private Location location;
    private String title;
    private boolean isActive;
    private Integer maxAmountPeople;
    private Integer beds;
    private Integer bedRooms;
    private Integer bathRooms;
    private HousingType housingType;
    private List<PhotoDTO> photos;
    private Long ownerId;

    public static HousingDTO convertToDTO(Housing housing) {
        return HousingDTO.builder()
                .id(housing.getId())
                .title(housing.getTitle())
                .maxAmountPeople(housing.getMaxAmountPeople())
                .beds(housing.getBeds())
                .bathRooms(housing.getBathRooms())
                .bedRooms(housing.getBedRooms())
                .location(housing.getLocation())
                .price(housing.getPrice())
                .description(housing.getDescription())
                .housingType(housing.getHousingType())
                .ownerId(housing.getOwner().getId())
                .isActive(housing.isActive())
                .photos(housing.getPhotos().stream()
                        .map(PhotoDTO::convertToDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Housing convertToEntity(HousingDTO housingDTO) {
        Housing housing = new Housing();
        housing.setId(housingDTO.getId());
        housing.setTitle(housingDTO.getTitle());
        housing.setMaxAmountPeople(housingDTO.getMaxAmountPeople());
        housing.setBeds(housingDTO.getBeds());
        housing.setBathRooms(housingDTO.getBathRooms());
        housing.setBedRooms(housingDTO.getBedRooms());
        housing.setPrice(housingDTO.getPrice());
        housing.setDescription(housingDTO.getDescription());
        housing.setHousingType(housingDTO.getHousingType());
        housing.setActive(housingDTO.isActive());
        housing.setLocation(housingDTO.getLocation());

        List<Photo> photoEntities = housingDTO.getPhotos().stream()
                .map(PhotoDTO::convertToPhoto)
                .collect(Collectors.toList());
        housing.setPhotos(photoEntities);

        return housing;
    }
}
