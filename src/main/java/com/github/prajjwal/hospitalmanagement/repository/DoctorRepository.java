package com.github.prajjwal.hospitalmanagement.repository;

import com.github.prajjwal.hospitalmanagement.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
