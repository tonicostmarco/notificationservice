package com.tonicostmarco.notificationservice.consumer;

import com.tonicostmarco.notificationservice.model.dtos.PaymentDTO;
import com.tonicostmarco.notificationservice.model.entities.Payment;
import com.tonicostmarco.notificationservice.repositories.PaymentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PaymentConsumer {

    private final PaymentRepository repository;

    public PaymentConsumer(PaymentRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "payment.failed")
    public void failedPayments(PaymentDTO dto) {

        Payment payment = new Payment();

        if (!repository.existsByTransactionId(dto.transactionId())) {
            turnIntoDto(dto, payment);
            repository.save(payment);
        }

    }

    @RabbitListener(queues = "payment.paid")
    public void paidPayments(PaymentDTO dto) {

        Payment payment = new Payment();

        if (!repository.existsByTransactionId(dto.transactionId())) {
            turnIntoDto(dto, payment);
            repository.save(payment);

        }

    }

    @RabbitListener(queues = "payment.pending")
    public void pendingPayments(PaymentDTO dto) {

        Payment payment = new Payment();

        if (!repository.existsByTransactionId(dto.transactionId())) {
            turnIntoDto(dto, payment);
            repository.save(payment);
        }

    }

    private void turnIntoDto(PaymentDTO dto, Payment payment) {

        payment.setAmount(dto.amount());
        payment.setStatus(dto.status());
        payment.setCustomerEmail(dto.customerEmail());
        payment.setProcessedAt(Instant.now());
        payment.setTransactionId(dto.transactionId());

    }

}
