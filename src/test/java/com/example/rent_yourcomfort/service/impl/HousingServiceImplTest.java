package com.example.rent_yourcomfort.service.impl;

import com.example.rent_yourcomfort.configuration.JwtService;
import com.example.rent_yourcomfort.dto.HousingDTO;
import com.example.rent_yourcomfort.model.*;
import com.example.rent_yourcomfort.repository.HousingRepository;
import com.example.rent_yourcomfort.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HousingServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HousingRepository housingRepository;

    @InjectMocks
    private HousingServiceImpl housingService;

    @BeforeEach
    void setUp() {}

    @Test
    public void testFindByCity_whenHousingsFound() {
        // Arrange
        String city = "Kharkiv";
        List<Housing> housings = new ArrayList<>();
        housings.add(createHousing(city));
        housings.add(createHousing(city));
        when(housingRepository.findAll()).thenReturn(housings);

        // Act
        List<Housing> result = housingService.findByCity(city);

        // Assert
        assertEquals(2, result.size());
        for (Housing housing : result) {
            assertEquals(city, housing.getLocation().getCity());
        }
    }
    private Housing createHousing(String city) {
        Location location = new Location();
        location.setCity(city);
        Housing housing = new Housing();
        housing.setLocation(location);
        return housing;
    }

    @Test
    public void testFindByCity_whenNoHousingsFound() {
        // Arrange
        String city = "Kharkiv";
        List<Housing> housings = new ArrayList<>();
        when(housingRepository.findAll()).thenReturn(housings);

        // Act
        List<Housing> result = housingService.findByCity(city);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCity_whenHousingsInDifferentCities() {
        // Arrange
        String city = "Kharkiv";
        List<Housing> housings = new ArrayList<>();
        housings.add(createHousing("Kyiv"));
        housings.add(createHousing("Lviv"));
        when(housingRepository.findAll()).thenReturn(housings);

        // Act
        List<Housing> result = housingService.findByCity(city);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCity_whenCityIsNull() {
        // Arrange
        List<Housing> housings = new ArrayList<>();
        housings.add(createHousing(null));
        when(housingRepository.findAll()).thenReturn(housings);

        // Act
        List<Housing> result = housingService.findByCity(null);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCity_whenCityCaseInsensitive() {
        // Arrange
        String city = "kharkiv";
        List<Housing> housings = new ArrayList<>();
        housings.add(createHousing("Kharkiv"));
        housings.add(createHousing("Kharkiv"));
        when(housingRepository.findAll()).thenReturn(housings);

        // Act
        List<Housing> result = housingService.findByCity(city);

        // Assert
        assertEquals(2, result.size());
        for (Housing housing : result) {
            assertEquals("Kharkiv", housing.getLocation().getCity());
        }
    }

    @Test
    void testCreateHousing() throws IOException {

        HousingDTO housingDTO = new HousingDTO();
        housingDTO.setTitle("Title");
        housingDTO.setDescription("Description");
        housingDTO.setMaxAmountPeople(4);
        housingDTO.setBeds(2);
        housingDTO.setBedRooms(2);
        housingDTO.setBathRooms(1);
        housingDTO.setPrice(BigDecimal.valueOf(100.0));
        housingDTO.setHousingType(HousingType.APARTMENT);
        housingDTO.setActive(true);
        housingDTO.setLocation(new Location());

        User user = new User();
        user.setEmail("test@example.com");
        user.setId(1L); // Убедитесь, что у пользователя есть id
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Mock file processing
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        MultipartFile[] files = new MultipartFile[]{file};

        Housing housing = new Housing();
        Location location = new Location();
        housing.setLocation(location);
        location.setHousing(housing);
        housing.setTitle("Title");
        housing.setDescription("Description");
        housing.setPrice(BigDecimal.valueOf(100.0));
        housing.setOwner(user); // Устанавливаем владельца

        when(housingRepository.save(any(Housing.class))).thenReturn(housing);

        // Setup security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Call the method to test
        HousingDTO result = housingService.createHousing(housingDTO, files);

        // Verify results
        verify(userRepository).findByEmail("test@example.com");
        verify(housingRepository).save(any(Housing.class));

        assertEquals("Title", result.getTitle());
        assertEquals("Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(100.0), result.getPrice());
    }

    @Test
    void testCreateHousingNoAuthentication() {
        // Arrange
        HousingDTO housingDTO = new HousingDTO();
        housingDTO.setDescription("Description");
        housingDTO.setTitle("Title");
        housingDTO.setMaxAmountPeople(4);
        housingDTO.setBeds(2);
        housingDTO.setBedRooms(2);
        housingDTO.setBathRooms(1);
        housingDTO.setPrice(BigDecimal.valueOf(100.0));
        housingDTO.setHousingType(HousingType.APARTMENT);
        housingDTO.setActive(true);
        housingDTO.setLocation(new Location());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            housingService.createHousing(housingDTO, new MultipartFile[0]);
        });
    }
}
