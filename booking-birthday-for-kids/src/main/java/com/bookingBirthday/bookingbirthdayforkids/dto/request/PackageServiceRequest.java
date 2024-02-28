package com.bookingBirthday.bookingbirthdayforkids.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageServiceRequest {
    private String serviceId;
    private String packageId;
    private String count;
    private String pricing;
}