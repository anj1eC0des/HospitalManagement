package com.example.BillingService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.BillingService.entity.*;
import com.example.BillingService.repository.MessageOutBoxRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.BillingService.repository.BillingRepository;

@Service
@Slf4j
public class BillService {

    private final BillingRepository billingRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MessageOutBoxRepo messageOutBoxRepo;

    public BillService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public Bill creatBill(BillDetailsDTO bill) {
        Bill b = new Bill();
        b.setPatientId(bill.getPatientId());
        b.setAppointmentId(bill.getAppointmentId());
        b.setPaymentStatus(bill.getPaymentStatus());
        b.setVersion(bill.getVersion());
        Bill savedBill=billingRepository.save(b);
        publishMessageAsync(savedBill);
        return savedBill;
    }

    public List<Bill> listBills() {
        return billingRepository.findAll();
    }

    public Optional<Bill> getBill(int id) {
        // bill find by id
        return billingRepository.findById(id);
    }

    public Bill updateBill(int id, BillDetailsDTO bill) throws Exception{
        Bill b = billingRepository.findById(id).orElseThrow(()->new Exception("Bill not found."));
        b.setVersion(bill.getVersion());
        b.setPaymentStatus(bill.getPaymentStatus());
        b.setPatientId(bill.getPatientId());
        b.setAppointmentId(bill.getAppointmentId());
        return b;
    }

    public void updateBill(BillDetailsDTO bill) {
        Bill b = billingRepository.searchByAppointmentId(bill.getAppointmentId());
        if(bill.getVersion()>b.getVersion()){
            b.setPatientId(bill.getPatientId());
            b.setPaymentStatus(bill.getPaymentStatus());
        }
        else{
            log.info("Bill with old version.");
        }
        billingRepository.save(b);
    }

    public void deleteBill(int id) {
        billingRepository.deleteById(id);
    }

    public List<Bill> searchBillByPatientId(int id) {
        return billingRepository.searchByPatientId(id);
    }

    public void processAppointment(String m){
        try{
            String message=objectMapper.readValue(m,String.class);
            AppointmentDTO appointment=objectMapper.readValue(message, AppointmentDTO.class);
            if(Objects.equals(appointment.getStatus(), "SCHEDULED")){
                creatBill(new BillDetailsDTO(appointment.getPatientId(),
                        appointment.getAppointmentId(),
                        "CREATED",
                        appointment.getVersion()));
            } else if (Objects.equals(appointment.getStatus(), "COMPLETED")) {
                updateBill(new BillDetailsDTO(appointment.getPatientId(),
                        appointment.getAppointmentId(),
                        "OVERDUE",
                        appointment.getVersion()));
            }
        }catch(JsonProcessingException j) {
            log.warn(j.getMessage());
            log.warn("Could not process json");
        }
        catch(Exception e){
            log.warn("cannot map appointment json");
        }
    }

    @Async
    public void publishMessageAsync(Bill bill){
        BillDetailsDTO billDTO= BillDetailsDTO.from(bill);
        try{
            String billJson=objectMapper.writeValueAsString(billDTO);
            rabbitTemplate.convertAndSend("bill.queue",billJson);
            log.info("Bill message sent");
        }catch (AmqpException a){
            MessageOutBox ob=new MessageOutBox();
            ob.setBillId(bill.getBillId());
            ob.setPatientId(bill.getPatientId());
            ob.setStatus(bill.getPaymentStatus());
            ob.setProcessed(false);
            ob.setTimeStamp(LocalDateTime.now());
            messageOutBoxRepo.save(ob);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn("Could not parse JSON!");
        }
    }
}
