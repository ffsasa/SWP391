package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicesRequest {
    @NotBlank(message = "Services name cannot be blank")
    private String serviceName;

    @NotBlank(message = "Description of services name cannot be blank")
    private String description;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;
}
