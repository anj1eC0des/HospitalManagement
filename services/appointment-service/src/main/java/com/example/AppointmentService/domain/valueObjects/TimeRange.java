package com.example.AppointmentService.domain.valueObjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.AssertTrue;

import java.time.Duration;
import java.time.LocalTime;

@Embeddable
public record TimeRange (LocalTime startTime,LocalTime endTime){
    @AssertTrue
    public boolean startTimeIsAlwaysLessThanEndTime(){
        return this.startTime!=null
                && this.endTime!=null
                && this.startTime.isBefore(this.endTime);
    }
    @AssertTrue
    public boolean theDurationIsGreaterThan15MinutesAtleast(){
        return Duration.between(startTime,endTime).compareTo(Duration.ofMinutes(15))>=0;
    }
    @AssertTrue
    public boolean theDurationCantBeGreaterThanTwoHours(){
        return Duration.between(startTime,endTime).compareTo(Duration.ofHours(2))<=0;
    }

}
