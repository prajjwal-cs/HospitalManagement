package com.github.prajjwal.hospitalmanagement;

import com.github.prajjwal.hospitalmanagement.model.Patient;
import com.github.prajjwal.hospitalmanagement.model.type.BloodGroupType;
import com.github.prajjwal.hospitalmanagement.repository.PatientRepository;
import com.github.prajjwal.hospitalmanagement.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PatientTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepository() {
        List<Patient> patientList = patientRepository.findAll();
        System.out.println("Patients list = " + patientList);
    }

    @Test
    public void testTransactionMethods() {
        Page<Patient> patientList = patientRepository.findAllPatients(PageRequest
                .of(0, 10, Sort.by("name"))
        );
        for (Patient patient : patientList) {
            System.out.println(patient);
        }
    }

    @Test
    public void testCreatePatient() {
        Patient patient = new Patient();
        patient.setName("John");
        patient.setGender("MALE");
        patient.setEmail("john2@gmail.com");
        patient.setBirthDate(LocalDate.of(1980, 6, 1));
        patient.setBloodGroup(BloodGroupType.A_POSITIVE);

        Patient savedPatient = patientRepository.save(patient);

        assertThat(savedPatient).isNotNull();
        assertThat(savedPatient.getId()).isNotNull();
        assertThat(savedPatient.getName()).isEqualTo("John");
        assertThat(savedPatient.getEmail()).isEqualTo("john2@gmail.com");
    }
}