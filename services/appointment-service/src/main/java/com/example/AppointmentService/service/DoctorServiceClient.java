package com.example.AppointmentService.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "doctor-service",
        fallbackFactory = DoctorServiceClient.DoctorServiceClientFallback.class)
public interface DoctorServiceClient {

    @GetMapping("/doctors/{id}")
    @CircuitBreaker(name = "doctor-service",fallbackMethod = "existsDoctorFallback")
    @Retry(name = "doctor-service")
    Object existsDoctor(@PathVariable int id);

    @Component
    class DoctorServiceClientFallback implements DoctorServiceClient {
        @Override
        public Object existsDoctor(int id) {
            return null;
        }
    }
}
