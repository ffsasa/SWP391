package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {
    @NotBlank(message = "Package name cannot blank")
    private String packageName ;
    @NotBlank(message = "PackageImgUrl cannot blank")
    private String packageImgUrl;
    private float  pricing;
}
