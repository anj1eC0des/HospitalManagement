package com.example.AppointmentService.entity;

import com.example.AppointmentService.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Component
@Slf4j
public class OutBoxProcessor {
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutBoxProcessor(OutboxRepository outboxRepository,RabbitTemplate rabbitTemplate){
        this.outboxRepository=outboxRepository;
        this.rabbitTemplate=rabbitTemplate;
    }

    @Scheduled(fixedDelay = 30000) // Every 30 seconds
    @Transactional
    public void processOutboxEvents() {
        List<OutBoxEntity> unprocessedEntities = outboxRepository.findUnprocessedEntities();
        unprocessedEntities.sort(Comparator.comparing(OutBoxEntity::getTimeStamp));
        for (OutBoxEntity entity : unprocessedEntities) {
            try {
                rabbitTemplate.convertAndSend(entity.getQueue(), entity.getAppointmentJson());
                entity.setProcessed(true);
                outboxRepository.save(entity);
            } catch (Exception e) {
                log.warn("Failed to process outbox event {}: {}", entity.getId(), e.getMessage());
            }
        }
    }
}
