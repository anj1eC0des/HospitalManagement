package com.example.AppointmentService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox")
public class OutBoxEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "queue",nullable = false)
    private String queue;
    @Column(name = "appointmentJson",nullable = false,columnDefinition = "TEXT")
    private String appointmentJson;
    @Column(name = "processed", nullable = false)
    private boolean processed;
    @Column(name = "timestamp",nullable = false)
    private LocalDateTime timeStamp;

}
