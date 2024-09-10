package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.configuration.SecurityUtil;
import com.example.rent_yourcomfort.dto.HousingDTO;
import com.example.rent_yourcomfort.dto.LocationDTO;
import com.example.rent_yourcomfort.dto.PhotoDTO;
import com.example.rent_yourcomfort.exception.HousingNotFoundException;
import com.example.rent_yourcomfort.exception.UnauthorizedException;
import com.example.rent_yourcomfort.model.*;
import com.example.rent_yourcomfort.repository.BookingRepository;
import com.example.rent_yourcomfort.repository.HousingRepository;
import com.example.rent_yourcomfort.repository.PhotoRepository;
import com.example.rent_yourcomfort.repository.UserRepository;
import com.example.rent_yourcomfort.service.HousingService;
import com.example.rent_yourcomfort.service.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.rent_yourcomfort.dto.HousingDTO.convertToDTO;
import static com.example.rent_yourcomfort.dto.HousingDTO.convertToEntity;
import static com.example.rent_yourcomfort.dto.PhotoDTO.convertToPhoto;

@Slf4j
@Service
public class HousingServiceImpl implements HousingService {

    private final HousingRepository housingRepository;
    private final PhotoRepository photoRepository;
    private final PhotoService photoService;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    public HousingServiceImpl(HousingRepository housingRepository, BookingRepository bookingRepository, PhotoRepository photoRepository, PhotoService photoService, UserRepository userRepository) {
        this.housingRepository = housingRepository;
        this.photoRepository = photoRepository;
        this.photoService = photoService;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Housing> findByCity(String city) {
        List<Housing> housings = housingRepository.findAll();
        List<Housing> result = new ArrayList<>();
        for (Housing housing : housings) {
            Location location = housing.getLocation();
            if (location != null && location.getCity() != null && location.getCity().equalsIgnoreCase(city)) {
                result.add(housing);
            }
        }
        return result;
    }

    @Override
    public List<Housing> findByMaxNumberOfPeopleThatCanBeAccommodated(int maxAmountPeople) {
        List<Housing> housings = housingRepository.findAll();
        List<Housing> result = new ArrayList<>();
        for (Housing housing : housings) {
            if (housing.getMaxAmountPeople() == maxAmountPeople) {
                result.add(housing);
            }
        }
        return result;
    }

    @Override
    public HousingDTO createHousing(HousingDTO housingDTO, MultipartFile[] files) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userInfo = authentication.getName();
        User owner = userRepository.findByEmail(userInfo).orElseThrow();

        Housing housing = new Housing();
        housing.setDescription(housingDTO.getDescription());
        housing.setTitle(housingDTO.getTitle());
        housing.setMaxAmountPeople(housingDTO.getMaxAmountPeople());
        housing.setBeds(housingDTO.getBeds());
        housing.setBedRooms(housingDTO.getBedRooms());
        housing.setBathRooms(housingDTO.getBathRooms());
        housing.setPrice(housingDTO.getPrice());
        housing.setHousingType(housingDTO.getHousingType());
        housing.setActive(housingDTO.isActive());

        owner.addRole(Role.OWNER);
        housing.setOwner(owner);

        // Создание и сохранение объекта Location
        housing.setLocation(housingDTO.getLocation());
        housing.getLocation().setHousing(housing);

        List<Photo> photoEntities = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setFileName(file.getOriginalFilename());
            photoDTO.setData(file.getBytes());
            photoDTO.setHousing(housing);
            photoEntities.add(convertToPhoto(photoDTO));
        }
        housing.setPhotos(photoEntities);

        // Сохраняем Housing после всех изменений
        housing = housingRepository.save(housing);

        return convertToDTO(housing);
    }

    @Override
    public HousingDTO updateHousing(Long housingId, HousingDTO housingDTO, MultipartFile[] files) throws IOException {
        String currentUsername = SecurityUtil.getCurrentUsername();
        if (currentUsername == null) {
            throw new UnauthorizedException("You are not authorized to update this housing.");
        }

        Housing housingEntity = housingRepository.findById(housingId)
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + housingId));

        // Check if the owner of the housing matches the current user
        if (!housingEntity.getOwner().getUsername().equals(currentUsername)) {
            throw new UnauthorizedException("You are not authorized to update this housing.");
        }

        // Update fields of HousingEntity based on HousingDTO
        housingEntity.setTitle(housingDTO.getTitle());
        housingEntity.setDescription(housingDTO.getDescription());
        housingEntity.setMaxAmountPeople(housingDTO.getMaxAmountPeople());
        housingEntity.setBeds(housingDTO.getBeds());
        housingEntity.setBedRooms(housingDTO.getBedRooms());
        housingEntity.setBathRooms(housingDTO.getBathRooms());
        housingEntity.setPrice(housingDTO.getPrice());
        housingEntity.setHousingType(housingDTO.getHousingType());
        housingEntity.setActive(housingDTO.isActive());

        Location location = housingEntity.getLocation();
        if (location != null) {
            location.setCountry(housingDTO.getLocation().getCountry());
            location.setRegion(housingDTO.getLocation().getRegion());
            location.setCity(housingDTO.getLocation().getCity());
            location.setStreet(housingDTO.getLocation().getStreet());
            location.setHouseNumber(housingDTO.getLocation().getHouseNumber());
            location.setApartmentNumber(housingDTO.getLocation().getApartmentNumber());
            location.setZipCode(housingDTO.getLocation().getZipCode());
        } else {
            location = new Location();
            location.setCountry(housingDTO.getLocation().getCountry());
            location.setRegion(housingDTO.getLocation().getRegion());
            location.setCity(housingDTO.getLocation().getCity());
            location.setStreet(housingDTO.getLocation().getStreet());
            location.setHouseNumber(housingDTO.getLocation().getHouseNumber());
            location.setApartmentNumber(housingDTO.getLocation().getApartmentNumber());
            location.setZipCode(housingDTO.getLocation().getZipCode());
            location.setHousing(convertToEntity(housingDTO));
            housingDTO.setLocation(location);
        }

        // Update photos of HousingEntity based on files
        if (files != null && files.length > 0) {
            List<Photo> photoEntities = new ArrayList<>();
            for (MultipartFile file : files) {

                List<Photo> photoList = housingEntity.getPhotos();
                for (Photo photoDTO1 : photoList) {
                    photoDTO1.setId(photoDTO1.getId());
                    photoDTO1.setFileName(file.getOriginalFilename());
                    photoDTO1.setData(file.getBytes());
                    photoEntities.add(photoDTO1);
                }
            }
            housingEntity.setPhotos(photoEntities);
        }
        Housing savedHousing = housingRepository.save(housingEntity);

        return convertToDTO(savedHousing);
    }

    @Override
    public List<PhotoDTO> getPhotosByHousingId(Long housingId) {
        List<PhotoDTO> photoDTOList = new ArrayList<>();
        Optional<Housing> housingOptional = housingRepository.findById(housingId);
        if (housingOptional.isPresent()) {
            Housing housing = housingOptional.get();
            List<Photo> photos = housing.getPhotos();
            if (photos != null) {
                photoDTOList = photos.stream()
                        .map(photo -> PhotoDTO.builder()
                                .id(photo.getId())
                                .fileName(photo.getFileName())
                                .data(photo.getData())
                                .build())
                        .collect(Collectors.toList());
            }
        }
        return photoDTOList;
    }

    @Override
    public Photo getImageById(Long housingId, Long imageId) {
        log.info("Start method getPhotoByIdFromHousingId");
        Housing housing = housingRepository.findById(housingId).orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + housingId));
        Photo photo = null;
        if (housing.getPhotos() != null) {
            for (Photo p : housing.getPhotos()) {
                if (p.getId().equals(imageId) && p.getId() != null) {
                    photo = p;
                    log.info("We found our image by id " + photo);
                    break;
                }
            }
        } else {
            throw new EntityNotFoundException(" Image this id not found ");
        }
        return photo;
    }

    @Override
    public List<HousingDTO> getAllHousing() {
        List<Housing> housingEntities = housingRepository.findAll();
        return housingEntities.stream()
                .map(HousingDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHousing(Long id) {
        Optional<Housing> housingEntityOptional = housingRepository.findById(id);

        if (housingEntityOptional.isPresent()) {
            Housing housingEntity = housingEntityOptional.get();
            housingRepository.delete(housingEntity);
            System.out.println("Housing delete successfully");
        } else {
            throw new NullPointerException("Housing not found with id: " + id);
        }
    }

    @Override
    public void deleteImageByIdFromHousingId(Long housingId, Long imageId) {
        Housing housing = housingRepository.findById(housingId)
                .orElseThrow(() -> new EntityNotFoundException("Housing not found with id " + housingId));

        Photo photo = housing.getPhotos().stream()
                .filter(p -> p.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id " + imageId));

        // Удаляем фото из коллекции
        housing.getPhotos().remove(photo);
        housingRepository.save(housing);

        // Удаляем фото из базы данных
        photoRepository.deleteById(imageId);
    }

    @Override
    public HousingDTO getHousingById(Long id) {

        Optional<Housing> housingOptional = housingRepository.findById(id);
        if (housingOptional.isPresent()) {
            return housingOptional.map(HousingDTO::convertToDTO).orElse(null);
        } else {
            throw new HousingNotFoundException(id.toString());
        }
    }

    @Override
    public List<Housing> getAvailableHousings(Date startDate, Date endDate) {
        List<Housing> allHousing = housingRepository.findAll();
        Set<Housing> unavailableHousings = allHousing.stream()
                .filter(housing -> housing.getBookings().stream()
                        .anyMatch(booking -> !Objects.equals(booking.getStatus(), "rejected") &&
                                isOverlapping(booking.getStartDate(), booking.getEndDate(), startDate, endDate)))
                .collect(Collectors.toSet());
        allHousing.removeAll(unavailableHousings);
        return allHousing;
    }

    private boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    @Override
    public List<Housing> getBookedHousing(Date startDate, Date endDate) {
        List<Housing> allHousing = housingRepository.findAll();
        return allHousing.stream()
                .filter(housing -> housing.getBookings().stream()
                        .anyMatch(booking -> Objects.equals(booking.getStatus(), "approved") && isOverlapping(booking.getStartDate(), booking.getEndDate(), startDate, endDate)))
                .collect(Collectors.toList());
    }

    private Location mapToLocation(LocationDTO location){
        return Location.builder()
                .country(location.getCountry())
                .region(location.getRegion())
                .city(location.getCity())
                .street(location.getStreet())
                .houseNumber(location.getHouseNumber())
                .apartmentNumber(location.getApartmentNumber())
                .zipCode(location.getZipCode())
                .build();

    }
}
