package com.example.rent_yourcomfort.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает нулевые (null) поля из вывода JSON
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Игнорирует Hibernate-специфические поля
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Property extends Housing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private HousingType housingType;
    private Integer bedRooms;
    private Integer maxAmountPeople;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private Integer beds;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private List<Photo> photos;

    @JsonIgnoreProperties({"location"})
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location location;

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

