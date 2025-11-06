package com.github.prajjwal.hospitalmanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseDto {
    private Long appointmentId;
    private LocalDateTime appointmentTime;
    private String specialization;
    private PatientResponseDto patient;
    private DoctorResponseDto doctor;
}