package com.example.AppointmentService.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class BusinessRuleViolationException extends RuntimeException {
    private final List<String> violations;

    public BusinessRuleViolationException(List<String> violations) {
        super("Business rules violated: " + String.join(", ", violations));
        this.violations = violations;
    }

}
