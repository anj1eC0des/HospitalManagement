package com.example.patientService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="patientHistory")
public class PatientHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    @Column(name="conditionName",nullable = false)
    String conditionName;
    @Column(name="diagnosedDate",nullable = false)
    LocalDate diagnosedDate;
    @Column(name = "isActive")
    boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="patients",nullable = false)
    Patient patient;
}
