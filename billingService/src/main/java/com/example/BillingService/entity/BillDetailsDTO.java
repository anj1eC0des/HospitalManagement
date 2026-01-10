package com.example.BillingService.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillDetailsDTO {
    private int patientId;
    private int appointmentId;
    private String paymentStatus;
    private Long version;

    public static BillDetailsDTO from(Bill bill){
        return new BillDetailsDTO(bill.getPatientId(),
                bill.getAppointmentId(),
                bill.getPaymentStatus(),
                bill.getVersion());
    }
}
