package com.github.prajjwal.hospitalmanagement.controller;

import com.github.prajjwal.hospitalmanagement.dto.AppointmentResponseDto;
import com.github.prajjwal.hospitalmanagement.dto.CreateAppointmentRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.PatientResponseDto;
import com.github.prajjwal.hospitalmanagement.service.AppointmentService;
import com.github.prajjwal.hospitalmanagement.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;


    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
            @RequestBody CreateAppointmentRequestDto appointmentRequestDto) {
        AppointmentResponseDto appointment = appointmentService.createNewAppointment(appointmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @GetMapping("profile")
    public ResponseEntity<PatientResponseDto> getPatientByIdOrEmail(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String email) {
        PatientResponseDto patient;
        if (id != null && email == null) {
            patient = patientService.getPatientById(id);
        }  else {
            patient = patientService.getPatientByEmail(email);
        }
        return ResponseEntity.ok(patient);
    }


}