package com.example.AppointmentService.domain.valueObjects;

import java.time.DayOfWeek;

public record WorkingHour(DayOfWeek dayOfWeek, TimeRange timeRange) {
}
