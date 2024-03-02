package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpgradeServiceRequest {
    @Min(value = 1, message = "Count value must be greater than or equal to 1")
    private int count;
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float pricing;
}
