package com.example.AppointmentService.domain.aggregate;


import com.example.AppointmentService.domain.valueObjects.Doctor;
import com.example.AppointmentService.domain.valueObjects.TimeRange;

import java.time.LocalDate;
import java.util.List;

public record Appointment(
        LocalDate dateOfAppointment,
        Doctor doctor,
        TimeRange timeRange,
        List<TimeRange> patientAppointments
) {
    //dont use dtos as fields explore anti-corruption layer and value objects
}
