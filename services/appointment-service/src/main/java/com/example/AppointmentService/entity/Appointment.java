package com.example.AppointmentService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointment {
    public enum AppointmentStatus {
        PENDING,        // created but not confirmed
        CONFIRMED,      // patient arrived
        IN_PROGRESS,    // with doctor
        COMPLETED,      // service done
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;
    @Column(name = "doctorId", nullable = false)
    private int doctorId;
    @Column(name = "patientId", nullable = false)
    private int patientId;
    @Column(name = "appointmentDateTime", nullable = false)
    private LocalDateTime appointmentDateTime;
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status=AppointmentStatus.PENDING;
    @Version
    private long version;

    //methods to enforce appointment state machine
    public void setAppointmentConfirmed(){
        if(this.getStatus()!=AppointmentStatus.PENDING)
            throw new IllegalStateException("Only pending Apponintments can be confirmed.");
        this.status=AppointmentStatus.CONFIRMED;
    }

    public void setAppointmentInProgress(){
        if(this.getStatus()!=AppointmentStatus.CONFIRMED)
            throw new IllegalStateException("Only Confirmed Appointments can be marked In-Progress");
        this.status=AppointmentStatus.IN_PROGRESS;
    }

    public void setAppointmentCompleted(){
        if(this.getStatus()!=AppointmentStatus.CONFIRMED
                && this.getStatus()!=AppointmentStatus.IN_PROGRESS)
            throw new IllegalStateException("Only Confirmed or In-Progress Appointments can be marked In-Progress");
        this.status=AppointmentStatus.COMPLETED;
    }

    public void setAppointmentCancelled(){
        if(this.getStatus()==AppointmentStatus.COMPLETED
        || this.getStatus()==AppointmentStatus.CANCELLED)
            throw new IllegalStateException("Already Completed or Cancelled Appointments cannot be cancelled again.");
        this.status=AppointmentStatus.CANCELLED;
    }
}
