package com.example.BillingService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageOutBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "billId", nullable = false)
    private int BillId;
    @Column(name = "patientId",nullable = false)
    private int patientId;
    @Column(name = "message",nullable = false)
    private String status;
    @Column(name = "processed", nullable = false)
    private boolean processed;
    @Column(name = "timestamp",nullable = false)
    private LocalDateTime timeStamp;
}
