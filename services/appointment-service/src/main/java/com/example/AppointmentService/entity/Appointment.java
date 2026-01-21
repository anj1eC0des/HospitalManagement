package com.example.AppointmentService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


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
    private Long appointmentId;
    @Column(name = "doctorId", nullable = false)
    private Integer doctorId;
    @Column(name = "patientId", nullable = false)
    private Integer patientId;
    @Column(name = "appointmentDate", nullable = false)
    private LocalDate appointmentDate;
    @Column(name= "appointmentStartTime",nullable = false)
    private LocalTime appointmentStartTime;
    @Column(name="appointmentEndTime",nullable=false)
    private LocalTime appointmentEndTime;
    @Column(name="appointmentDuration",nullable = false)
    private Duration appointmentDuration;
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status=AppointmentStatus.PENDING;
    @Version
    private Long version;

    //methods to enforce appointment state machine
    public void setAppointmentConfirmed(){
        if(this.getStatus()!=AppointmentStatus.PENDING)
            throw new IllegalStateException("Only pending Appointments can be confirmed.");
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

    @AssertTrue
    boolean appointmentStartTimeIsAlwaysLessThanAppointmentEndTime(){
        return this.appointmentStartTime!=null &&
                this.appointmentEndTime!=null &&
                this.appointmentStartTime.isBefore(this.appointmentEndTime);
    }

    @AssertTrue
    boolean appointmentDurationCanNeverBeBelow15Mins(){
        return !this.appointmentDuration.isZero() &&
                this.appointmentDuration.compareTo(Duration.ofMinutes(15))>=0;
    }
}
