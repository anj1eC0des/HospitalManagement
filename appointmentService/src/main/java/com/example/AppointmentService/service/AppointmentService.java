package com.example.AppointmentService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.AppointmentService.entity.AppointmentDetailsDTO;
import com.example.AppointmentService.entity.OutBoxEntity;
import com.example.AppointmentService.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.AppointmentService.entity.Appointment;
import com.example.AppointmentService.entity.AppointmentDTO;
import com.example.AppointmentService.repository.AppointmentRepository;

@Service
@Slf4j
public class AppointmentService {
    private final RabbitTemplate rabbitTemplate;
    private final AppointmentRepository appointmentRepository;
    private final PatientServiceClient patientServiceClient;
    private final DoctorServiceClient doctorServiceClient;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OutboxRepository outboxRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
            PatientServiceClient patientServiceClient,
            DoctorServiceClient doctorServiceClient,
            RabbitTemplate rabbitTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.patientServiceClient = patientServiceClient;
        this.doctorServiceClient = doctorServiceClient;
        this.rabbitTemplate=rabbitTemplate;
    }

    public Appointment creatAppointment(AppointmentDetailsDTO appointment) throws Exception {
        CompletableFuture<Object> patientExists= CompletableFuture.supplyAsync(()-> patientServiceClient.existsPatient(appointment.getPatientId()));
        CompletableFuture<Object> doctorExists= CompletableFuture.supplyAsync(()->doctorServiceClient.existsDoctor(appointment.getDoctorId()));
        CompletableFuture.allOf(patientExists,doctorExists)
                .orTimeout(10, TimeUnit.SECONDS)
                .join();
        if(patientExists.get()!=null && doctorExists.get()!=null){
            Appointment appt= new Appointment();
            appt.setPatientId(appointment.getPatientId());
            appt.setDoctorId(appointment.getDoctorId());
            appt.setAppointmentDateTime(appointment.getAppointmentDateTime());
            appt.setStatus(appointment.getStatus());
            Appointment savedappt= appointmentRepository.save(appt);
            publishMessageAsync("appointment.queue", savedappt);
            return savedappt;
        }
        else throw new EntityNotFoundException("Doctor/Patient not found");
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointment(int id) {
        return appointmentRepository.findById(id);
    }


    public Appointment updateAppointment(int id, AppointmentDetailsDTO appointment) {
        Appointment appt= appointmentRepository
                .findById(id)
                .orElseThrow(()->new EntityNotFoundException("Appt not found"));
        appt.setPatientId(appointment.getPatientId());
        appt.setDoctorId(appointment.getDoctorId());
        appt.setAppointmentDateTime(appointment.getAppointmentDateTime());
        appt.setStatus(appointment.getStatus());
        Appointment updatedappt = appointmentRepository.save(appt);
        publishMessageAsync("appointment.queue",updatedappt);
        return updatedappt;
    }

    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> searchAppointmentByPatientId(int patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> searchAppointmentByDoctorId(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Async
    public void publishMessageAsync(String queue, Appointment appt){
        AppointmentDTO apptDTO= AppointmentDTO.from(appt);
        try{
            String apptJson= objectMapper.writeValueAsString(apptDTO);
            try{
                rabbitTemplate.convertAndSend(queue,apptJson);
            }catch (AmqpException a){
                System.out.println(a.getMessage());
                OutBoxEntity ob=new OutBoxEntity();
                ob.setQueue(queue);
                ob.setAppointmentJson(apptJson);
                ob.setProcessed(false);
                ob.setTimeStamp(LocalDateTime.now());
                outboxRepository.save(ob);
            }
        } catch (JsonProcessingException j){
            log.warn("Object could not be p");
        }
    }
}