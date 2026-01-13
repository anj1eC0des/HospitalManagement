package com.example.doctorService.entity;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkingHoursDTO(
        @NotNull DayOfWeek dayOfTheWeek,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime){
    public static WorkingHoursDTO dtoFromEntity(WorkingHours workingHours){
        return new WorkingHoursDTO(workingHours.getDayOfTheWeek(),
                workingHours.getStartTime(),
                workingHours.getEndTime());
    }

    public WorkingHours entityFromDto(Doctor doctor){
        WorkingHours workingHours=new WorkingHours();
        workingHours.setDoctor(doctor);
        workingHours.setDayOfTheWeek(this.dayOfTheWeek());
        workingHours.setStartTime(this.startTime());
        workingHours.setEndTime(this.endTime());
        return workingHours;
    }
}
