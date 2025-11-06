package com.github.prajjwal.hospitalmanagement.service;

import com.github.prajjwal.hospitalmanagement.dto.InsuranceRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.InsuranceResponseDto;
import com.github.prajjwal.hospitalmanagement.dto.PatientResponseDto;
import com.github.prajjwal.hospitalmanagement.model.Insurance;
import com.github.prajjwal.hospitalmanagement.model.Patient;
import com.github.prajjwal.hospitalmanagement.repository.InsuranceRepository;
import com.github.prajjwal.hospitalmanagement.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public PatientResponseDto assignInsuranceToPatient(InsuranceRequestDto insuranceRequest, Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new EntityNotFoundException("Patient with id: " + patientId + " not found")
        );
        Insurance insurance = modelMapper.map(insuranceRequest, Insurance.class);
        patient.setInsurance(insurance);
        insurance.setPatient(patient);

        insuranceRepository.save(insurance);
        patientRepository.save(patient);
        return modelMapper.map(patient, PatientResponseDto.class);
    }

    public InsuranceResponseDto createInsurance(InsuranceRequestDto insuranceRequest) {
        Insurance insurance = modelMapper.map(insuranceRequest, Insurance.class);
        insuranceRepository.save(insurance);
        return modelMapper.map(insurance, InsuranceResponseDto.class);
    }

    @Transactional
    public PatientResponseDto disassociateInsuranceFromPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new EntityNotFoundException("Patient with id: " + patientId + " not found")
        );
        Insurance insurance = patient.getInsurance();
        if(insurance != null) {
            insurance.setPatient(null);
            insuranceRepository.save(insurance);
        }
        patient.setInsurance(null);
        patientRepository.save(patient);

        return modelMapper.map(patient, PatientResponseDto.class);
    }
}