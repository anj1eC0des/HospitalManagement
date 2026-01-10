package com.example.BillingService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillDTO {
    private int billId;
    private int patientId;
    private int appointmentId;
    private String paymentStatus;
    private Long version;
}
