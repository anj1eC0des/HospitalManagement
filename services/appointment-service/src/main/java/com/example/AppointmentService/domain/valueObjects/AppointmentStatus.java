package com.example.AppointmentService.domain.valueObjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record AppointmentStatus(
        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        StatusCode statusCode) {
    public enum StatusCode {
        PENDING,        // created but not confirmed
        CONFIRMED,      // patient arrived
        IN_PROGRESS,    // with doctor
        COMPLETED,      // service done
        CANCELLED
    }

    public boolean canChangeStatus(StatusCode newStatusCode){
        return notAlreadyCompletedOrCancelled() && switch(newStatusCode){
            case PENDING -> false;
            case CONFIRMED -> canSetAppointmentConfirmed();
            case IN_PROGRESS -> canSetAppointmentInProgress();
            case CANCELLED,COMPLETED -> true;
        };
    }
    public boolean canSetAppointmentConfirmed(){
        return this.statusCode()== StatusCode.PENDING;
    }
    public boolean canSetAppointmentInProgress(){
        return this.statusCode== StatusCode.CONFIRMED;
    }
    public boolean notAlreadyCompletedOrCancelled(){
    return this.statusCode!=StatusCode.COMPLETED
            && this.statusCode!=StatusCode.CANCELLED;
    }
}
