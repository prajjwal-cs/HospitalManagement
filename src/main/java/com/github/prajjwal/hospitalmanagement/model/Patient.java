package com.github.prajjwal.hospitalmanagement.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.prajjwal.hospitalmanagement.model.type.BloodGroupType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(
        name = "patient",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_patient_name_birthdate", columnNames = {"name", "birthDate"})
        },
        indexes = {
                @Index(name = "idx_patient_birth_date", columnList = "birthDate")
        }
)
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 40)
    private String name;

    private LocalDate birthDate;

    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String gender;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_insurance_id")
    private Insurance insurance;

    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();
}