package com.example.AppointmentService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.AppointmentService.entity.Appointment;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId")
    public List<Appointment> findByPatientId(int patientId);

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId")
    public List<Appointment> findByDoctorId(int doctorId);
}
