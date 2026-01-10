package com.example.BillingService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int billId;
    @Column(name = "patientId", nullable = false)
    private int patientId;
    @Column(name = "appointmentId", nullable = false)
    private int appointmentId;
    @Column(name = "paymentStatus", nullable = false)
    private String paymentStatus;
    @Column(name = "version", nullable = false)
    private Long version;
}
