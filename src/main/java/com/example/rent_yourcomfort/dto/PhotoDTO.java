package com.example.rent_yourcomfort.dto;

import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
    private Long id;
    private String fileName;
    private Housing housing;
    private byte[] data;

    public static PhotoDTO convertToDTO(Photo photoEntity) {
        return PhotoDTO.builder()
                .id(photoEntity.getId())
                .fileName(photoEntity.getFileName())
                .housing(photoEntity.getHousing())
                .data(photoEntity.getData())
                .build();
    }

    public static Photo convertToPhoto(PhotoDTO photoDTO){
        Photo photo = new Photo();
        photo.setId(photoDTO.getId());
        photo.setFileName(photoDTO.getFileName());
        photo.setHousing(photoDTO.getHousing());
        photo.setData(photoDTO.getData());
        return photo;
    }
    public PhotoDTO convertMultipartFileToPhotoDTO(MultipartFile file, Long id) {
        return PhotoDTO.builder()
                .id(id)
                .fileName(file.getOriginalFilename())
                .build();
    }

    private Photo mapPhotoDTOToPhoto(PhotoDTO photoDTO, Housing property) {
        Photo photo = new Photo();
        photo.setId(photoDTO.getId());
        photo.setFileName(photoDTO.getFileName());
        photo.setHousing(property); // Устанавливаем связь с недвижимостью
        photo.setData(photoDTO.getData());

        return photo;
    }
}
