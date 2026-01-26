package com.example.AppointmentService.domain.invariants;

import com.example.AppointmentService.domain.BusinessRuleViolationException;
import com.example.AppointmentService.domain.aggregate.Appointment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusinessRuleValidator {

    private final List<BusinessRule<Appointment>> appointmentRules;
    List<String> errorLogs;
    BusinessRuleValidator(List<BusinessRule<Appointment>> appointmentRules){
        this.appointmentRules=appointmentRules;
    }

    public void validate(Appointment appointment){
        errorLogs.clear();
        for(BusinessRule<Appointment> rule:appointmentRules){
            if(!rule.isOk(appointment)) errorLogs.add(rule.getMessage());
        }
        if(!errorLogs.isEmpty())
            throw new BusinessRuleViolationException(errorLogs);
    }

    List<String> getErrorLogs(){
        return this.errorLogs;
    }

}
