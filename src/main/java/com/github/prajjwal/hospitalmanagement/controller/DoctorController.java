package com.github.prajjwal.hospitalmanagement.controller;

import com.github.prajjwal.hospitalmanagement.dto.AppointmentResponseDto;
import com.github.prajjwal.hospitalmanagement.model.Appointment;
import com.github.prajjwal.hospitalmanagement.repository.AppointmentRepository;
import com.github.prajjwal.hospitalmanagement.repository.DoctorRepository;
import com.github.prajjwal.hospitalmanagement.service.AppointmentService;
import com.github.prajjwal.hospitalmanagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAllAppointmentsOfADoctor(doctorId));
    }
}