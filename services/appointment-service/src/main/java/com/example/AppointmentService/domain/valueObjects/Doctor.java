package com.example.AppointmentService.domain.valueObjects;

import java.util.List;

public record Doctor(String specialization, List<WorkingHour> workingHours) {
}
