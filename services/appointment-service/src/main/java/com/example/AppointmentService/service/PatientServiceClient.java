package com.example.AppointmentService.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "patientService",
fallback = PatientServiceClient.PatientServiceClientFallback.class)
public interface PatientServiceClient {

    @GetMapping("/patients/{id}")
    @CircuitBreaker(name = "patient-service")
    @Retry(name = "patient-service")
    Object existsPatient(@PathVariable int id);

    @Component
    class PatientServiceClientFallback implements PatientServiceClient {
        @Override
        public Object existsPatient(int id) {
            return null;
        }
    }
}
