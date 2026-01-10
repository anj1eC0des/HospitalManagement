package com.example.AppointmentService.repository;

import com.example.AppointmentService.entity.OutBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutBoxEntity,Integer> {

    @Query("SELECT o from OutBoxEntity o WHERE o.processed= false")
    List<OutBoxEntity> findUnprocessedEntities();
}
