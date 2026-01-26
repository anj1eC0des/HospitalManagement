package com.example.AppointmentService.domain.invariants;

import com.example.AppointmentService.domain.aggregate.Appointment;

import java.time.LocalTime;

@AppointmentRule
public class AppointmentTimeDoesntClashWithPatientsOtherAppointments
implements BusinessRule<Appointment> {
    @Override
    public boolean isOk(Appointment appointment) {
        LocalTime startTime=appointment.timeRange().startTime();
        LocalTime endTime=appointment.timeRange().endTime();
        return appointment.patientAppointments().stream()
                .noneMatch(timerange-> startTime.isAfter(timerange.startTime())&&
                        startTime.isBefore(timerange.endTime()) ||
                        endTime.isAfter(timerange.startTime()) &&
                                endTime.isBefore(timerange.endTime()));
    }

    @Override
    public String getMessage() {
        return "Appointment clashes with existing Patient Appointments.";
    }
}
