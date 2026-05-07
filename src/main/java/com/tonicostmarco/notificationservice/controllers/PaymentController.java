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
@RequestMapping(value = "/payments")
public class PaymentController {


    private final RabbitTemplate template;

    public PaymentController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> receivePayment(@RequestBody @Valid PaymentDTO dto) {
        template.convertAndSend("exchange", "payment." + dto.status(), dto);

        return ResponseEntity.ok(dto);

    }

}
