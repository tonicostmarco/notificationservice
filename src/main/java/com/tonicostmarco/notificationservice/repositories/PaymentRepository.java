package com.tonicostmarco.notificationservice.repositories;

import com.tonicostmarco.notificationservice.model.entities.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    boolean existsByTransactionId(String s);
}
