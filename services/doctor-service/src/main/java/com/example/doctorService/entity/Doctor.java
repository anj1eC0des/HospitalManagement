package com.example.doctorService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "specialization",nullable = false)
    private String specialization;
    @Column(name = "departmentId",nullable = false)
    private int departmentId;
    @Column(name = "contactInformation",nullable = false)
    private int contactInformation;
    @OneToMany(mappedBy = "doctor",cascade =CascadeType.ALL,orphanRemoval = true)
    private List<WorkingHours> workingHours=new ArrayList<>();
}
