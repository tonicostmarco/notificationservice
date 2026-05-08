package com.tonicostmarco.notificationservice.controllers;

import com.tonicostmarco.notificationservice.model.dtos.PaymentDTO;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payments/notify")
public class PaymentController {


    private final RabbitTemplate template;

    public PaymentController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> receivePayment(@RequestBody @Valid PaymentDTO dto) {

        System.out.println("Publicando mensagem: " + dto.transactionId() + " status: " + dto.status().toString().toLowerCase());

        template.convertAndSend("exchange", "payment." + dto.status().toString().toLowerCase(), dto);

        System.out.println("Mensagem enviada para o exchange");

        return ResponseEntity.ok(dto);

    }

}
