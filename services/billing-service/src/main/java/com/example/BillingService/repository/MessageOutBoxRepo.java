package com.example.BillingService.repository;

import com.example.BillingService.entity.MessageOutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageOutBoxRepo extends JpaRepository<MessageOutBox,Integer> {
    @Query("SELECT o from MessageOutBox o WHERE o.processed= false")
    List<MessageOutBoxRepo> findUnprocessedEntities();
}
