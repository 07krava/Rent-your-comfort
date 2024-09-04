package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.dto.PhotoDTO;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Photo;
import com.example.rent_yourcomfort.repository.PhotoRepository;
import com.example.rent_yourcomfort.service.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoServiceImpl implements PhotoService {

    private PhotoRepository photoRepository;
    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public PhotoDTO savePhoto(MultipartFile file, Housing housingEntity) throws IOException {
        Photo photoEntity = new Photo();
        photoEntity.setFileName(file.getOriginalFilename());
        photoEntity.setHousing(housingEntity);
        photoEntity.setData(file.getBytes());

        Photo savedPhotoEntity = photoRepository.save(photoEntity);

        return convertToDTO(savedPhotoEntity);
    }

    public List<PhotoDTO> savePhotos(MultipartFile[] files, Housing housingEntity) throws IOException {
        List<PhotoDTO> photoDTOS = new ArrayList<>();

        for (MultipartFile file : files) {
            photoDTOS.add(savePhoto(file, housingEntity));
        }

        return photoDTOS;
    }

    public PhotoDTO getPhotoById(Long id) {
        Photo photoEntity = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with id " + id));

        return convertToDTO(photoEntity);
    }

    public void deletePhotoById(Long id) {
        photoRepository.deleteById(id);
    }

    public void deletePhotosByHousingId(Long housingId) {
        photoRepository.deleteByHousingId(housingId);
    }

    private PhotoDTO convertToDTO(Photo photoEntity) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setId(photoEntity.getId());
        photoDTO.setFileName(photoEntity.getFileName());
        photoDTO.setData(photoEntity.getData());

        return photoDTO;
    }

    public List<PhotoDTO> getPhotosForProperty(Long propertyId) {
        List<Photo> photos = photoRepository.findByHousingId(propertyId);

        // Преобразуйте сущности Photo в DTO PhotoDTO
        return photos.stream().map(this::mapPhotoToDTO).collect(Collectors.toList());
    }
    private PhotoDTO mapPhotoToDTO(Photo photo) {
        PhotoDTO dto = new PhotoDTO();
        dto.setId(photo.getId());
        dto.setFileName(photo.getFileName());
        dto.setHousing(photo.getHousing());
        dto.setData(photo.getData());
        // Другие поля, если есть

        return dto;
    }
}

