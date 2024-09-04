package com.example.rent_yourcomfort.repository;

import com.example.rent_yourcomfort.model.Housing;
import com.example.rent_yourcomfort.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Housing, Long>, JpaSpecificationExecutor<Property> {
}
