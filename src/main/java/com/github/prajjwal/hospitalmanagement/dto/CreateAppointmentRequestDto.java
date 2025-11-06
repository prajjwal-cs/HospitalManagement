package com.github.prajjwal.hospitalmanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateAppointmentRequestDto {
    private Long doctorId;
    private Long patientId;
    private String specialization;
    private LocalDateTime appointmentTime;
    private String reason;
}