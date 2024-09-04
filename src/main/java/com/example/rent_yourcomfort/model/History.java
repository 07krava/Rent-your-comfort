package com.example.rent_yourcomfort.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL) // Исключает нулевые (null) поля из вывода JSON
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Игнорирует Hibernate-специфические поля
@Data
@Entity
@Table(name = "history")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUser;
    @Column(nullable = true)
    private String housingType;
    @Column(nullable = true)
    private int bedRooms;
    @Column(nullable = true)
    private int bathRooms;
    @Column(nullable = true)
    private int maxAmountPeople;
    @Column(nullable = true)
    private int beds;
    @Column(nullable = true)
    private String city;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date endDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date dateOfRequest;
    private BigDecimal MinPrice;
    private BigDecimal MaxPrice;

}

