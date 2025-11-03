package com.github.prajjwal.hospitalmanagement.model;

import com.github.prajjwal.hospitalmanagement.model.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "patient",
        indexes = {
                @Index(name = "idx_patient_birth_date", columnList = "birthDate")
        })
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate birthDate;

    private String gender;

    @OneToOne
    @MapsId
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "patient_isurance_id")
    private Insurance insurance;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Appointment> appointments = new ArrayList<>();
}