package com.example.patientService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int patientId;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name = "age",nullable = false)
    private int age;
    @Column(name = "gender",nullable = false)
    private String gender;
    @Column(name = "number",nullable = false)
    private int number;
    @Column(name = "address",nullable = false)
    private String address;
    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PatientHistory> patientHistoryList=new ArrayList<>();
}
