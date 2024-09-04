package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HousingRepository extends JpaRepository<Housing, Long> {

    List<Housing> findByIsActiveTrue();
}
