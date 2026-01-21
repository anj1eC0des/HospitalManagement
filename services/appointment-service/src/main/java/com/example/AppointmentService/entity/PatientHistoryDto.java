package com.example.AppointmentService.entity;

import java.time.LocalDate;

public record PatientHistoryDto(
        String conditionName,
        LocalDate diagnosedDate,
        boolean isActive){
}
