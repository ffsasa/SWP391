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
public class PackageRequest {
    @NotBlank(message = "Package name cannot blank")
    private String packageName ;
    private String packageImgUrl;
    @NotNull(message = "Pricing value cannot be null")
    @Min(value = 0, message = "Pricing value must be greater than or equal to 0")
    private float  pricing;
}
