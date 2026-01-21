package com.example.AppointmentService.entity;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingHoursDTO(
        @NotNull DayOfWeek dayOfTheWeek,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime){
}
