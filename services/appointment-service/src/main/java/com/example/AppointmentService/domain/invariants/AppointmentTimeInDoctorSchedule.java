package com.example.AppointmentService.domain.invariants;

import com.example.AppointmentService.domain.aggregate.Appointment;

import java.time.DayOfWeek;
import java.time.LocalTime;

@AppointmentRule
public class AppointmentTimeInDoctorSchedule implements BusinessRule<Appointment>{

    @Override
    public boolean isOk(Appointment appointment){
        DayOfWeek dayOfWeek= appointment.dateOfAppointment().getDayOfWeek();
        LocalTime startTime=appointment.timeRange().startTime();
        LocalTime endTime=appointment.timeRange().endTime();
        return appointment.doctor().workingHours().stream()
                .filter(wh-> wh.dayOfWeek()==dayOfWeek)
                .anyMatch(wh-> startTime.isAfter(wh.timeRange().startTime())
                        && startTime.isBefore(wh.timeRange().endTime())
                        && endTime.isAfter(wh.timeRange().startTime())
                        && endTime.isBefore(wh.timeRange().endTime()));
    }

    @Override
    public String getMessage(){
        return "Appointment can't be put in Doctors Schedule.";
    }
}
