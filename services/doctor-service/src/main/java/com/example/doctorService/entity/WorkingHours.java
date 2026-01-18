package com.example.doctorService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="workingHours")
public class WorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "dayOfTheWeek",nullable = false)
    private DayOfWeek dayOfTheWeek;
    @Column(name = "startTime",nullable = false)
    private LocalTime startTime;
    @Column(name = "endTime",nullable = false)
    private LocalTime endTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctors",nullable = false)
    private Doctor doctor;

    @AssertTrue(message = "the start-time should be before end-time")
    public boolean startTimeIsBeforeEndTime(){
        return startTime!=null && endTime!=null && startTime.isBefore(endTime);
    }

}
