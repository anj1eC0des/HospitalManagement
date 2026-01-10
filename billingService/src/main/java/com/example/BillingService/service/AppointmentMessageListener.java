package com.example.BillingService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AppointmentMessageListener {
    @Autowired
    private BillService billService;

    /**
     * Main queue listener - processes appointment messages
     */
    @RabbitListener(queues = "appointment.queue")
    public void handleAppointmentMessage(String message) {
        try {
            log.info("Received appointment message: {}", message);
            billService.processAppointment(message);
            log.info("Successfully processed appointment message");
        } catch (Exception e) {
            log.error("Error processing appointment message: {}", message, e);
            // Throwing this exception will send message to DLQ
            throw new AmqpRejectAndDontRequeueException("Failed to process appointment", e);
        }
    }

    /**
     * Dead Letter Queue listener - handles failed messages
     */
//    @RabbitListener(queues = "appointment.queue.dlq")
//    public void handleDeadLetterMessage(String message) {
//        try {
//            log.warn("Processing message from DLQ: {}", message);
//
//            // Your DLQ processing logic here
//            boolean reprocessed = billService.reprocessFailedAppointment(message);
//
//            if (reprocessed) {
//                log.info("Successfully reprocessed message from DLQ");
//            } else {
//                log.error("Failed to reprocess message from DLQ");
//            }
//
//        } catch (Exception e) {
//            log.error("Error processing DLQ message: {}", message, e);
//        }
//    }
}
