package com.example.AppointmentService.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppointmentDTO {
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private LocalDateTime appointmentDateTime;
    private String status;
    private Long version;

    public static AppointmentDTO from(Appointment appt) {
        return new AppointmentDTO(appt.getAppointmentId(), appt.getDoctorId(),
                appt.getPatientId(),
                appt.getAppointmentDateTime(),
                appt.getStatus(),
                appt.getVersion());
    }
}
