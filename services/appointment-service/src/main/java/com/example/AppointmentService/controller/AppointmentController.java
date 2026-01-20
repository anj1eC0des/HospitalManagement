package com.example.AppointmentService.controller;

import java.util.List;
import java.util.Optional;

import com.example.AppointmentService.entity.AppointmentDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.AppointmentService.entity.Appointment;
import com.example.AppointmentService.service.AppointmentService;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments")
    @ResponseBody
    public AppointmentDTO createAppointment(
            @Validated(AppointmentDTO.CreateInstance.class)
            @RequestBody AppointmentDTO appointmentDTO)throws Exception {
        return appointmentService.createAppointment(appointmentDTO);
    }

    @GetMapping("/appointments")
    @ResponseBody
    public List<AppointmentDTO> getAppointments() {
        return appointmentService.listAppointments();
    }

    @GetMapping("/appointments/{id}")
    @ResponseBody
    public AppointmentDTO getAppointments(@PathVariable int id) {
        return appointmentService.getAppointment(id);
    }

    @PutMapping("/appointments/{id}")
    @ResponseBody
    public AppointmentDTO updateAppointments(
            @Validated(AppointmentDTO.CreateInstance.class)
            @RequestBody AppointmentDTO patient,
            @PathVariable int id) throws Exception {
        return appointmentService.updateAppointment(id, patient);
    }

    @DeleteMapping("/appointments/{id}")
    @ResponseBody
    public void deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
    }

    @GetMapping("/appointments/patient/{patientId}")
    @ResponseBody
    public List<AppointmentDTO> searchAppointmentsByPatientId(@PathVariable int patientId) {
        return appointmentService.searchAppointmentByPatientId(patientId);
    }

    @GetMapping("/appointments/doctor/{doctorId}")
    @ResponseBody
    public List<AppointmentDTO> searchAppointmentsByDoctorId(@PathVariable int doctorId) {
        return appointmentService.searchAppointmentByDoctorId(doctorId);
    }
}
