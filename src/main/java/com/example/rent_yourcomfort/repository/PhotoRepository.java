package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    void deleteByHousingId(Long housingId);

    List<Photo> findByHousingId(Long housingId);
}
