package com.example.rent_yourcomfort.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "photo")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "housing_id", referencedColumnName = "id")
    @ToString.Exclude
    private Housing housing;

    @Column(name = "data", nullable = false, columnDefinition = "mediumblob")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "property_id") // имя колонки для связи
    private Property property;

    public Photo(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Photo photo = (Photo) o;
        return id != null && Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
