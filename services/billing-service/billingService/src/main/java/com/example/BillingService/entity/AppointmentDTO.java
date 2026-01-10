package com.example.BillingService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private LocalDateTime appointmentDateTime;
    private String status;
    private long version;
}
