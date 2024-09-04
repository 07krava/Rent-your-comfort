package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.dto.HousingDTO;
import com.example.rent_yourcomfort.dto.PhotoDTO;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface HousingService {

    List<Housing> findByCity(String city);

    HousingDTO createHousing(HousingDTO housingDTO, MultipartFile[] files) throws IOException;

    HousingDTO updateHousing(Long id, HousingDTO housingDTO, MultipartFile[] files) throws IOException;

    List<PhotoDTO> getPhotosByHousingId(Long housingId);

    Photo getImageById(Long housingId, Long photoId);

    List<HousingDTO> getAllHousing();

    HousingDTO getHousingById(Long id);

    void deleteHousing(Long id);

    void deleteImageByIdFromHousingId(Long housingId, Long imageId);

    List<Housing> getBookedHousing(Date startDate, Date endDate);

    List<Housing> getAvailableHousings(Date startDate, Date endDate);

    List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(int maxAmountPeople);
}