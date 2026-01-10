package com.example.BillingService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.BillingService.entity.Bill;

@Repository
public interface BillingRepository extends JpaRepository<Bill, Integer> {
    @Query("SELECT b FROM Bill b WHERE b.patientId= :patientId")
    public List<Bill> searchByPatientId(int patientId);

    @Query("SELECT b FROM Bill b WHERE b.appointmentId= :appointmentId")
    public Bill searchByAppointmentId(int appointmentId);
}
