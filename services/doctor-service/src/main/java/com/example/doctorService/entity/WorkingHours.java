package com.example.doctorService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="workingHours")
public class WorkingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "dayOfTheWeek")
    private String dayOfTheWeek;
    @Column(name = "startTime")
    private String startTime;
    @Column(name = "endTime")
    private String endTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor",nullable = false)
    private Doctor doctor;

}
