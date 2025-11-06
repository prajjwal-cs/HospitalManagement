package com.github.prajjwal.hospitalmanagement.service;

import com.github.prajjwal.hospitalmanagement.dto.DoctorRequestDto;
import com.github.prajjwal.hospitalmanagement.dto.DoctorResponseDto;
import com.github.prajjwal.hospitalmanagement.model.Doctor;
import com.github.prajjwal.hospitalmanagement.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    public List<DoctorResponseDto> getAllDoctors(Integer page, Integer size) {
        log.info("Fetching doctors - page: {}, size: {}", page, size);
        Page<DoctorResponseDto> doctorPage = doctorRepository.findAll(PageRequest.of(page, size))
                .map(doctor -> modelMapper.map(doctor, DoctorResponseDto.class));
        return doctorPage.getContent();
    }

    public DoctorResponseDto getDoctorById(Long doctorId) {
        var doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id: " + doctorId + " not found"));
        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    public DoctorResponseDto createDoctor(DoctorRequestDto doctorRequest) {
        var doctor = modelMapper.map(doctorRequest, Doctor.class);
        doctorRepository.save(doctor);
        return modelMapper.map(doctor, DoctorResponseDto.class);
    }

    @Transactional
    public DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto doctorRequest) {
        var existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor with id: " + doctorId + " not found"));
        if (doctorRequest.getName() != null) {
            existingDoctor.setName(doctorRequest.getName());
        }
        if (doctorRequest.getEmail() != null) {
            existingDoctor.setEmail(doctorRequest.getEmail());
        }
        if (doctorRequest.getSpecialization() != null) {
            existingDoctor.setSpecialization(doctorRequest.getSpecialization());
        }

        doctorRepository.save(existingDoctor);
        return modelMapper.map(existingDoctor, DoctorResponseDto.class);
    }

    public void deleteDoctor(Long doctorId) {
        doctorRepository.deleteById(doctorId);
    }
}