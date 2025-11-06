package com.github.prajjwal.hospitalmanagement.service;

import com.github.prajjwal.hospitalmanagement.dto.AppointmentResponseDto;
import com.github.prajjwal.hospitalmanagement.dto.CreateAppointmentRequestDto;
import com.github.prajjwal.hospitalmanagement.model.Appointment;
import com.github.prajjwal.hospitalmanagement.model.Doctor;
import com.github.prajjwal.hospitalmanagement.model.Patient;
import com.github.prajjwal.hospitalmanagement.repository.AppointmentRepository;
import com.github.prajjwal.hospitalmanagement.repository.DoctorRepository;
import com.github.prajjwal.hospitalmanagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public AppointmentResponseDto createNewAppointment(CreateAppointmentRequestDto appointmentRequest) {
        long doctorId = appointmentRequest.getDoctorId();
        long patientId = appointmentRequest.getPatientId();
        String specialization = appointmentRequest.getSpecialization();

        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new EntityNotFoundException("Patient not found" + patientId)
        );
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() ->
                new EntityNotFoundException("Doctor not found" + doctorId)
        );

        Appointment appointment = Appointment.builder()
                .reason(appointmentRequest.getReason())
                .appointmentTime(appointmentRequest.getAppointmentTime())
                .build();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        patient.getAppointments().add(appointment);

        appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }

    @Transactional
    public Appointment reAssignAppointToAnotherDoctor(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() ->
                new EntityNotFoundException("Appointment not found" + appointmentId)
        );
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() ->
                new EntityNotFoundException("Doctor not found" + doctorId)
        );

        appointment.setDoctor(doctor);
        doctor.getAppointments().add(appointment);
        return appointment;
    }

    public List<AppointmentResponseDto> getAllAppointmentsOfADoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        return doctor.getAppointments()
                .stream()
                .map(appointment ->
                        modelMapper.map(appointment, AppointmentResponseDto.class)
                )
                .toList();
    }
}