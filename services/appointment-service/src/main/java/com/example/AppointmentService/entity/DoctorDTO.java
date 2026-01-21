package com.example.AppointmentService.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;


public record DoctorDTO(
        @Null(groups = CreateInstance.class)
        @NotNull(groups = UpdateInstance.class)
        Long id,
        @NotNull String name,
        @NotNull String specialization,
        @NotNull Integer departmentId,
        @NotNull Integer contactInformation,
        @NotNull List<WorkingHoursDTO> workingHours){
    public interface CreateInstance{}
    public interface UpdateInstance{}
}
