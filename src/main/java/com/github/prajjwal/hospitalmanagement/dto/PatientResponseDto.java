package com.github.prajjwal.hospitalmanagement.dto;

import com.github.prajjwal.hospitalmanagement.model.type.BloodGroupType;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private Long patientId;
    private String name;
    @Email
    private String email;
    private LocalDate birthDate;
    private String gender;
    private BloodGroupType bloodGroup;
}