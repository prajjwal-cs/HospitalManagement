package com.github.prajjwal.hospitalmanagement.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class DoctorRequestDto {
    private String name;
    @Email
    private String email;
    private String specialization;
}