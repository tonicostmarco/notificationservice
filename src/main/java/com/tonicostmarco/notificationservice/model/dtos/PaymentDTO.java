package com.tonicostmarco.notificationservice.model.dtos;

import com.tonicostmarco.notificationservice.model.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentDTO(
        @NotBlank
        String transactionId,
        @NotNull
        Double amount,
        @NotNull
        Status status,
        @NotBlank
        @Size(min = 3, max = 80, message = "Email must have between 3 and 80 characters")
        @Email(message = "Insert a valid e-mail")
        String customerEmail) {

        public PaymentDTO {
        }
}
