package com.example.AppointmentService.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.hibernate.sql.Update;

import java.time.LocalDateTime;

public record AppointmentDTO(
    @Null(groups = CreateInstance.class)
    @NotNull(groups = UpdateInstance.class)
    int id,
    int doctorId,
    int patientId,
    LocalDateTime appointmentDateTime,
    Appointment.AppointmentStatus status){
    public interface CreateInstance{}
    public interface UpdateInstance{}

    public static AppointmentDTO dtoFromEntity(Appointment appointment){
        return new AppointmentDTO(
                appointment.getAppointmentId(),
                appointment.getDoctorId(),
                appointment.getPatientId(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
        );
    }

    public Appointment entityFromDto(){
        Appointment appointment=new Appointment();
        appointment.setDoctorId(this.doctorId());
        appointment.setPatientId(this.patientId());
        appointment.setAppointmentDateTime(this.appointmentDateTime());
        return appointment;
    }
}
