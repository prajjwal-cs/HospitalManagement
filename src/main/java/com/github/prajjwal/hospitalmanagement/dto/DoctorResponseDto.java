package com.github.prajjwal.hospitalmanagement.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseDto {
    private Long doctorId;
    private String name;
    private String specialization;
    @Email
    private String email;
}