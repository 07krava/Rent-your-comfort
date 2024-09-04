package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.Feedback;
import com.example.rent_yourcomfort.model.Housing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByHousingId(Housing housing);
}
