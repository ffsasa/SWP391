package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PaymentMethodRequest {
    @NotBlank(message = "Method cannot be blank")
    private String methodName;
    @NotBlank(message = "Description cannot be blank")
    private String methodDescription;

}
