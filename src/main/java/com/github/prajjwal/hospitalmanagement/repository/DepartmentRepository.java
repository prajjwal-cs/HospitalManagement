package com.github.prajjwal.hospitalmanagement.repository;

import com.github.prajjwal.hospitalmanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
