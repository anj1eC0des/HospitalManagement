package com.example.AppointmentService.domain.invariants;

import org.springframework.stereotype.Component;

@Component
public interface BusinessRule<T> {
    boolean isOk(T object);
    String getMessage();
}
