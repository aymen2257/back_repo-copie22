package com.espaceadherent.repo;

import com.espaceadherent.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment findBySessionId(String sessionId);

    List<Payment> findByUserId(Long userId);
}
