package com.example.rent_yourcomfort.controller;

import com.example.rent_yourcomfort.dto.HousingDTO;
import com.example.rent_yourcomfort.dto.PhotoDTO;
import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.service.HousingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/housing/")
public class HousingController {

    @Autowired
    private HousingService housingService;

    @GetMapping("/searchByCity/{city}")
    public List<Housing> searchByCity(@PathVariable("city") String city, @ModelAttribute Housing housing) {
        return housingService.findByCity(city);
    }

    @GetMapping("/allHousing")
    public List<HousingDTO> getAllHousing() {
        return housingService.getAllHousing();
    }

    @PutMapping("/updateHousing/{id}")
    public HousingDTO updateHousing(@PathVariable Long id, @ModelAttribute HousingDTO housingDTO,
                                    @RequestParam("file") MultipartFile[] files) throws IOException {
        housingDTO.setId(id);
        return housingService.updateHousing(id, housingDTO, files);
    }

    @DeleteMapping("/deleteHousing/{id}")
    public ResponseEntity<String> deleteHousingById(@PathVariable Long id) {
        housingService.deleteHousing(id);
        return new ResponseEntity<>("Housing " + id + " delete successfully!", HttpStatus.OK);
    }

    @GetMapping("/getHousing/{id}")
    public ResponseEntity<?> getHousingById(@PathVariable Long id) {

        try{
            HousingDTO housingDTO = housingService.getHousingById(id);
            return  ResponseEntity.status(HttpStatus.OK).body(housingDTO);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Housing not found with ID: " + id + ".");
        }
    }

    @GetMapping("/getImagesByHousing/{id}")
    public List<PhotoDTO> getPhotoByHousingId(@PathVariable Long id) {
        return housingService.getPhotosByHousingId(id);
    }

    @GetMapping("/{housingId}/image/{imageId}")
    public ResponseEntity<PhotoDTO> getPhotoById(@PathVariable Long housingId, @PathVariable Long imageId) {
        log.info("Here start method getPhotoById");
        HousingDTO housing = housingService.getHousingById(housingId);
        PhotoDTO photo = null;
        if (housing.getPhotos() != null) {
            for (PhotoDTO p : housing.getPhotos()) {
                if (p.getId().equals(imageId) && p.getId() != null) {
                    photo = p;
                    break;
                }
            }
        }
        if (photo == null) {
            log.info("This message you can see if your image not found");
            throw new EntityNotFoundException("Image not found with this id: " + imageId);
        }
        return ResponseEntity.ok(photo);
    }

    @DeleteMapping("/{housingId}/deleteImage/{imageId}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long housingId, @PathVariable Long imageId) {
        try {
            housingService.deleteImageByIdFromHousingId(housingId, imageId);
            return ResponseEntity.ok("The image was successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
        }
    }

    @GetMapping("/available")
    public List<Housing> getAvailableHousing(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return housingService.getAvailableHousings(startDate, endDate);
    }

    @GetMapping("/booked")
    public List<Housing> getBookedHousing(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return housingService.getBookedHousing(startDate, endDate);
    }

    @GetMapping("/findByMaxPeople/{maxValuePeople}")
    public List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(@PathVariable("maxValuePeople") int maxValuePeople, @ModelAttribute Housing housing) {
        return housingService.findByMaxNumberOfPeopleThatCanBeAccommodated(maxValuePeople);
    }
}
