package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeServiceRequest {
    @Min(value = 1, message = "Count value must be greater than or equal to 1")
    private int count;
    @NotNull(message = "Service ID cannot be null")
    private Long serviceId;
}
