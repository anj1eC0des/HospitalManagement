package com.example.AppointmentService.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDTO(
    @Null(groups = CreateInstance.class)
    @NotNull(groups = UpdateInstance.class)
    Long id,
    Integer doctorId,
    Integer patientId,
    LocalDate appointmentDate,
    LocalTime appointmentStartTime,
    LocalTime appointmentEndTime,
    Duration appointmentDuration,
    Appointment.AppointmentStatus status){
    public interface CreateInstance{}
    public interface UpdateInstance{}

    public static AppointmentDTO dtoFromEntity(Appointment appointment){
        return new AppointmentDTO(
                appointment.getAppointmentId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentStartTime(),
                appointment.getAppointmentEndTime(),
                appointment.getAppointmentDuration(),
                appointment.getStatus()
        );
    }

    public Appointment entityFromDto(){
        Appointment appointment=new Appointment();
        appointment.setDoctorId(this.doctorId());
        appointment.setPatientId(this.patientId());
        appointment.setAppointmentDate(this.appointmentDate());
        appointment.setAppointmentStartTime(this.appointmentStartTime());
        appointment.setAppointmentEndTime(this.appointmentEndTime());
        appointment.setAppointmentDuration(this.appointmentDuration());
        return appointment;
    }
}
