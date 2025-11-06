package com.github.prajjwal.hospitalmanagement.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 100)
    private String specialization;

    @Email
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @JsonBackReference
    @ManyToMany(mappedBy = "doctors", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Department> departments = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Appointment> appointments = new ArrayList<>();
}