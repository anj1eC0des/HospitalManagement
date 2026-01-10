package com.example.AppointmentService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppointmentDetailsDTO {
    private int doctorId;
    private int patientId;
    private LocalDateTime appointmentDateTime;
    private String status;
}
