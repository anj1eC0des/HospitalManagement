package com.example.AppointmentService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.example.AppointmentService.entity.OutBoxEntity;
import com.example.AppointmentService.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.AppointmentService.entity.Appointment;
import com.example.AppointmentService.entity.AppointmentDTO;
import com.example.AppointmentService.repository.AppointmentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO appointment)
            throws ExecutionException, InterruptedException {
        CompletableFuture<Object> patientExists= CompletableFuture.supplyAsync(()->
                patientServiceClient.existsPatient(appointment.patientId()));
        CompletableFuture<Object> doctorExists= CompletableFuture.supplyAsync(()->
                doctorServiceClient.existsDoctor(appointment.doctorId()));
        CompletableFuture.allOf(patientExists,doctorExists)
                .orTimeout(10, TimeUnit.SECONDS)
                .join();
        if(patientExists.get()!=null && doctorExists.get()!=null){
            Appointment savedAppointment=
                    appointmentRepository.save(appointment.entityFromDto());
            AppointmentDTO savedAppointmentDto =AppointmentDTO.dtoFromEntity(savedAppointment);
            publishMessageAsync("appointment.queue", savedAppointmentDto);
            log.info("Appointment created successfully {} {} {}",
                    savedAppointment.getAppointmentId(),
                    savedAppointment.getDoctorId(),
                    savedAppointment.getPatientId());
            return savedAppointmentDto;
        }
        else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Doctor/Patient not found");
    }

    public List<AppointmentDTO> listAppointments() {
        log.info("List of appointments fetched.");
        return appointmentRepository.findAll().stream()
                .map(AppointmentDTO::dtoFromEntity)
                .toList();
    }

    public AppointmentDTO getAppointment(int id) {
        Appointment appointment=appointmentRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Appointment not found.")
        );
        return AppointmentDTO.dtoFromEntity(appointment);
    }


    public AppointmentDTO updateAppointment(int id, AppointmentDTO appointmentDTO) {
        Appointment appointment= appointmentRepository
                .findById(id)
                .orElseThrow(
                        ()->new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Appointment not found")
                );
        appointment.setPatientId(appointmentDTO.patientId());
        appointment.setDoctorId(appointmentDTO.doctorId());
        appointment.setAppointmentDateTime(appointmentDTO.appointmentDateTime());
        switch(appointmentDTO.status()){
            case PENDING -> throw new IllegalStateException(
                    "Status is "+appointment.getStatus()+" but tried set Pending."
            );
            case CONFIRMED -> appointment.setAppointmentConfirmed();
            case IN_PROGRESS -> appointment.setAppointmentInProgress();
            case COMPLETED -> appointment.setAppointmentCompleted();
            case CANCELLED -> appointment.setAppointmentCancelled();
        }
        AppointmentDTO updatedAppointmentDto = AppointmentDTO.dtoFromEntity(
                appointmentRepository.save(appointment));
        log.info("Appointment updated.");
        publishMessageAsync("appointment.queue", updatedAppointmentDto);
        return updatedAppointmentDto;
    }

    public void deleteAppointment(int id) {
        appointmentRepository.deleteById(id);
    }

    public List<AppointmentDTO> searchAppointmentByPatientId(int patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                        .map(AppointmentDTO::dtoFromEntity).toList();

    }

    public List<AppointmentDTO> searchAppointmentByDoctorId(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(AppointmentDTO::dtoFromEntity).toList();
    }

    @Async
    public void publishMessageAsync(String queue, AppointmentDTO appointmentDTO){
        try{
            String apptJson= objectMapper.writeValueAsString(appointmentDTO);
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