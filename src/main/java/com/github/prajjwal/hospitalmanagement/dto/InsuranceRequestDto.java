package com.github.prajjwal.hospitalmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InsuranceRequestDto {
    @NotBlank
    private String policyNumber;
    @NotBlank
    private String provider;
    @NotBlank
    private LocalDate validUntil;
}