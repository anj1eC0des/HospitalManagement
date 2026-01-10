package com.example.doctorService.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorDTO {
    private String name;
    private String specialization;
    private int departmentId;
    private int contactInformation;
}
