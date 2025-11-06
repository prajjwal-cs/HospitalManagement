package com.github.prajjwal.hospitalmanagement.repository;

import com.github.prajjwal.hospitalmanagement.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}