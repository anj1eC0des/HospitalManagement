package com.example.AppointmentService.entity;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.ArrayList;
import java.util.List;

public record PatientDTO(
    @NotNull(groups = UpdateInstance.class)
    @Null(groups = CreateInstance.class)
    Long id,
    String name,
    int age,
    String gender,
    int number,
    String address,
    List<PatientHistoryDto> patientHistory){
    public interface CreateInstance{}
    public interface UpdateInstance{}
}
