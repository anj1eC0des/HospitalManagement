package com.example.doctorService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkingHoursDto {
    private String dayOfTheWeek;
    private String startTime;
    private String endTime;
}
