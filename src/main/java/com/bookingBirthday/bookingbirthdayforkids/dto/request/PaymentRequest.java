package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class PaymentRequest {
    @Min(value = 1, message = "Min at least = 1")
    private float amount;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @NotNull(message = "Expire date cannot null")
    private LocalDateTime expireDate;
}
