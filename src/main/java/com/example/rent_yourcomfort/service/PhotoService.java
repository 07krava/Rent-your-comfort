package com.example.rent_yourcomfort.service;

import com.example.rent_yourcomfort.dto.PhotoDTO;
import com.example.rent_yourcomfort.model.Housing;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    PhotoDTO savePhoto(MultipartFile file, Housing housing) throws IOException;

    List<PhotoDTO> savePhotos(MultipartFile[] files, Housing housing) throws IOException;

    PhotoDTO getPhotoById(Long photoId);

    void deletePhotoById(Long id);

    void deletePhotosByHousingId(Long housingId);
}
