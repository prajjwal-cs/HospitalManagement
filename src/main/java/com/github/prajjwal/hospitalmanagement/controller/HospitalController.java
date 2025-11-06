package com.github.prajjwal.hospitalmanagement.controller;

import com.github.prajjwal.hospitalmanagement.dto.DoctorResponseDto;
import com.github.prajjwal.hospitalmanagement.service.AppointmentService;
import com.github.prajjwal.hospitalmanagement.service.DoctorService;
import com.github.prajjwal.hospitalmanagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class HospitalController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(doctorService.getAllDoctors(page, size));
    }
}