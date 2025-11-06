package com.github.prajjwal.hospitalmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequestDto {
    private String name;
    @Email
    @NotBlank
    private String email;
    private LocalDate birthDate;
    private String gender;

}