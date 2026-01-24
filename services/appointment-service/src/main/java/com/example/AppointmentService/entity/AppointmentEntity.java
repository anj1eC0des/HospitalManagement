package com.example.AppointmentService.entity;

import com.example.AppointmentService.domain.valueObjects.AppointmentStatus;
import com.example.AppointmentService.domain.valueObjects.TimeRange;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    @Column(name = "doctorId", nullable = false)
    private Integer doctorId;
    @Column(name = "patientId", nullable = false)
    private Integer patientId;
    @Column(name = "appointmentDate", nullable = false)
    private LocalDate appointmentDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="startTime",column = @Column(name= "startTime")),
            @AttributeOverride(name="endTime",column = @Column(name="endTime"))
    })
    private TimeRange timeRange;
    @Embedded
    private AppointmentStatus status= new AppointmentStatus(AppointmentStatus.StatusCode.PENDING);
    @Version
    private Long version;

}
