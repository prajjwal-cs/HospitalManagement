package com.github.prajjwal.hospitalmanagement.service;

import com.github.prajjwal.hospitalmanagement.dto.PatientRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.PatientResponseDto;
import com.github.prajjwal.hospitalmanagement.model.Patient;
import com.github.prajjwal.hospitalmanagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientResponseDto createNewPatient(PatientRequestDto request) {
        Patient patient = modelMapper.map(request, Patient.class);
        patientRepository.save(patient);
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    public PatientResponseDto updatePatient(Long patientId, PatientRequestDto request) {
//        Patient patient = modelMapper.map(request, Patient.class);
        Patient existingPatient = patientRepository.findById(patientId).orElseThrow(
                () -> new EntityNotFoundException("Patient not found with id: " + patientId)
        );

        if (request.getName() != null) {
            existingPatient.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingPatient.setEmail(request.getEmail());
        }
        if (request.getBirthDate() != null) {
            existingPatient.setBirthDate(request.getBirthDate());
        }
        if (request.getGender() != null) {
            existingPatient.setGender(request.getGender());
        }
        Patient updatedPatient = patientRepository.save(existingPatient);

        return modelMapper.map(updatedPatient, PatientResponseDto.class);
    }

    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new EntityNotFoundException("Patient with id: " + patientId + " not found"));
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        int page = (pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
        int size = (pageSize != null && pageSize >= 0) ? pageSize : 10;

        return patientRepository.findAllPatients(PageRequest.of(page, size))
                .stream()
                .map(patient -> modelMapper.map(patient, PatientResponseDto.class))
                .collect(Collectors.toList());
    }

    public void deletePatientById(Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);
        patientRepository.delete(patient);
    }

    public PatientResponseDto getPatientByEmail(String email) {
        return modelMapper.map(patientRepository.findByEmail(email),  PatientResponseDto.class);
    }
}