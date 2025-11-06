package com.github.prajjwal.hospitalmanagement.repository;

import com.github.prajjwal.hospitalmanagement.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
