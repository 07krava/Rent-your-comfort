package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByIdUser(Long userId);
}
