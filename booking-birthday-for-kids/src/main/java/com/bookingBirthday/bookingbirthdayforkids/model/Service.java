package com.bookingBirthday.bookingbirthdayforkids.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Service extends BaseEntity{
    @NotBlank(message = "Service name cannot be blank")
    private String serviceName;

    @NotBlank(message = "Description of service name cannot be blank")
    private String description;

    @NotNull(message = "Pricing of service cannot be null")
    private float pricing;


}
