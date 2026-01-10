package com.example.patientService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientDTO {
    private String name;
    private int age;
    private String gender;
    private int number;
    private String address;
}
