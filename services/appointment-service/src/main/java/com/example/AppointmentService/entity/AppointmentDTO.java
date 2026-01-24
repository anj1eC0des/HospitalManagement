package com.example.AppointmentService.entity;

import com.example.AppointmentService.domain.valueObjects.AppointmentStatus;
import com.example.AppointmentService.domain.valueObjects.TimeRange;
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
    AppointmentStatus.StatusCode status){
    public interface CreateInstance{}
    public interface UpdateInstance{}

    public static AppointmentDTO dtoFromEntity(AppointmentEntity appointmentEntity){
        LocalTime startTime=appointmentEntity.getTimeRange().startTime();
        LocalTime endTime= appointmentEntity.getTimeRange().endTime();
        return new AppointmentDTO(
                appointmentEntity.getAppointmentId(),
                appointmentEntity.getDoctorId(),
                appointmentEntity.getPatientId(),
                appointmentEntity.getAppointmentDate(),
                startTime,
                endTime,
                Duration.between(startTime,endTime),
                appointmentEntity.getStatus().statusCode()
        );
    }

    public AppointmentEntity entityFromDto(){
        if(this.status()!= AppointmentStatus.StatusCode.PENDING)
            throw new IllegalStateException("Can't create appointment entity with non-Pending Status Code");
        AppointmentEntity appointmentEntity =new AppointmentEntity();
        appointmentEntity.setDoctorId(this.doctorId());
        appointmentEntity.setPatientId(this.patientId());
        appointmentEntity.setAppointmentDate(this.appointmentDate());
        appointmentEntity.setTimeRange(new TimeRange(
                this.appointmentStartTime(),this.appointmentEndTime()));
        return appointmentEntity;
    }
}
