package com.github.prajjwal.hospitalmanagement.dto;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InsuranceResponseDto {
    private Long insuranceId;
    private String policyNumber;
    private String provider;
    private LocalDate validUntil;
    @CreationTimestamp
    private LocalDateTime createdAt;
}