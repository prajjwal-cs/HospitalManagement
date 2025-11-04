package com.github.prajjwal.hospitalmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @MapsId
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String specialization;

    @Column(unique = true, length = 100)
    private String email;


    @ManyToMany(mappedBy = "doctors")
    @ToString.Exclude
    private Set<Department> departments = new HashSet<>();

    @OneToMany(mappedBy = "doctor")
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();
}